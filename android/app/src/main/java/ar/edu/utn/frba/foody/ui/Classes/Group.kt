package ar.edu.utn.frba.foody.ui.Classes

data class Group (
    val groupId: Int = 0,
    var name: String = "",
    var password: String = "",
    var members: List<User> = emptyList(),
    val membersLimit: Int = 10,
)