package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User


class GroupViewModel() : ViewModel() {
    private var group by mutableStateOf(Group())

    fun updateGroup(newGroup: Group) {
        group = newGroup
    }

    fun getPickedGroup(): Group {
        return group
    }

    fun addUser(user: User) {
        val mutableList = group.members.toMutableList()
        mutableList.add(user)
        val updatedGroup = group.copy(members = mutableList.toList())
        group = updatedGroup
    }

    fun deleteUser(user: User): Group {
        val updatedGroup = group.copy(members = group.members.filter { it != user })
        group = updatedGroup
        return updatedGroup
    }
}