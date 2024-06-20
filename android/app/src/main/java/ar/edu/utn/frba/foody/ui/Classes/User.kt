package ar.edu.utn.frba.foody.ui.Classes

data class User(
    val userId: Int = 0,
    var email: String = "",
    var password: String = "",
    var direccion: Int = 0,
    var numeroContacto: Int = 0,
    var groupId: Int = 0,
    var admin: Boolean = false
)
