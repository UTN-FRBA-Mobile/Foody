package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.GroupDataBaseFirebase

class GroupViewModel() : ViewModel() {
    private var group by mutableStateOf(Group())
    var groupDataBaseFirebase: GroupDataBaseFirebase? = null
    var userLogged by mutableStateOf(User())
    fun setServices(
        groupDataBaseFirebase: GroupDataBaseFirebase,
    ) {
        this.groupDataBaseFirebase = groupDataBaseFirebase
    }
    fun updateGroup(newGroup: Group) {
        group = newGroup
    }
    fun getPickedGroup(): Group {
        return group
    }
    fun createGroup(newGroup: Group, admin: User): Group {
        this.updateGroup(newGroup)
        admin.admin = true
        this.addUser(admin)
        groupDataBaseFirebase?.insertGroup(group)
        return group
    }
    fun addUser(user: User) {
        val mutableList = group.members.toMutableList()
        mutableList.add(user)
        val updatedGroup = group.copy(members = mutableList.toList())
        group = updatedGroup
    }
    fun updateUser(user: User) {
        this.addUser(user)
        groupDataBaseFirebase?.addUser(group, user)
    }
    fun deleteUser(user: User, callback: (Group?) -> Unit) {
        groupDataBaseFirebase?.removeUser(group.groupId, user) {
            val updatedGroup: Group
            if (user.admin) {
                val newGroup = reassignAdmin(group)
                updatedGroup = group.copy(members = newGroup.members.filter { it != user })
            } else {
                updatedGroup = group.copy(members = group.members.filter { it != user })
            }
            if (updatedGroup.members.isEmpty()) {
                this.updateGroup(Group())
            } else {
                this.updateGroup(updatedGroup)
            }
            callback(updatedGroup)
        }
    }
    fun reassignAdmin(newGroup: Group): Group {
        var adminFound = false
        newGroup.members.forEach { member ->
            if (!member.admin && !adminFound) {
                member.admin = true
                adminFound = true
            }
        }
        return newGroup
    }
    fun verifyNameGroupExist(name: String, callback: (Group?) -> Unit) {
        groupDataBaseFirebase?.getGroupByName(name) { group ->
            if (group != null) {
                callback(group)
            } else {
                callback(null)
            }
        }
    }
    fun groupIsEmpty():Boolean{
        return group.groupId==""
    }
    fun verifyGroupExist(name: String, pass: String, callback: (Group?) -> Unit) {
        groupDataBaseFirebase?.getGroupByName(name) { group ->
            if (group != null && group.password == pass) {
                callback(group)
            } else {
                callback(null)
            }
        }
    }
    fun findGroupByUserId(){
        groupDataBaseFirebase?.getGroupByUserId(userLogged) { group ->
            if (group != null) {
                this.updateGroup(group)
            }
        }
    }
}