package ar.edu.utn.frba.foody.ui.Classes

data class User(
    var userId: String = "",
    var email: String = "",
    var password: String = "",
    var direccion: Address.AddressInfo = Address.AddressInfo(),
    var numeroContacto: Int = 0,
    var groupId: Int = 0,
    var admin: Boolean = false
)
