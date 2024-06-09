package ar.edu.utn.frba.foody.ui.dataClasses

import android.content.*
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User
import java.util.UUID

class GroupViewModel() : ViewModel() {
    private var group by mutableStateOf(Group())

    fun updateGroup(newGroup: Group) {
        group = newGroup
    }

    fun getPickedGroup(): Group {
        return group
    }

    fun createLink(context: Context) {
        val baseUrl = context.getString(R.string.base_url)
        val groupId = UUID.randomUUID().toString()
        val finalUrl = "$baseUrl/$groupId"

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, finalUrl)
        }

        context.startActivity(Intent.createChooser(intent, null))
    }

    fun deleteUser(user: User): Group {
        val updatedGroup = group.copy(members = group.members.filter { it != user })
        group = updatedGroup
        return updatedGroup
    }
}