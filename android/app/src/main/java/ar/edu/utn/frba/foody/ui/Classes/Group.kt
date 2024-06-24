package ar.edu.utn.frba.foody.ui.Classes

data class Group (
    var groupId: String = "",
    var name: String = "",
    var password: String = "",
    var members: List<User> = emptyList(),
    val membersLimit: Int = 10,
)