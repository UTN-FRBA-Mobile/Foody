package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.GroupDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.GroupDataBase


class GroupViewModel() : ViewModel() {
    private var group by mutableStateOf(Group())

    var groupDataBase: GroupDataBase? = null
    var groupDataBaseFirebase: GroupDataBaseFirebase? = null
    var navController: NavController? = null

    fun setServices(
        groupDataBase: GroupDataBase,
        groupDataBaseFirebase: GroupDataBaseFirebase,
        navController: NavController
    ) {
        this.groupDataBase = groupDataBase
        this.groupDataBaseFirebase = groupDataBaseFirebase
        this.navController = navController
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
        groupDataBaseFirebase?.updateGroup(group, user)
    }

    fun deleteUser(user: User): Group {
        val updatedGroup = group.copy(members = group.members.filter { it != user })
        group = updatedGroup
        return updatedGroup
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

    fun verifyGroupExist(name: String, pass: String, callback: (Group?) -> Unit) {
        groupDataBaseFirebase?.getGroupByName(name) { group ->
            if (group != null && group.password == pass) {
                callback(group)
            } else {
                callback(null)
            }
        }
    }

}
