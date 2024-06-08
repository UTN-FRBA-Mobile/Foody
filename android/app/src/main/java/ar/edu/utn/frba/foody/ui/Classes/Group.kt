package ar.edu.utn.frba.foody.ui.Classes

data class Group (
    val groupId: Int = 0,
    val name: String = "",
    var members: List<User> = emptyList(),
    val limit: Int = 10,
)