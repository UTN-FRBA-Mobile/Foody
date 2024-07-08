package ar.edu.utn.frba.foody.ui.Classes

data class User(
    var userId: String = "",
    var email: String = "",
    var password: String = "",
    var direccion: Address.AddressInfo = Address.AddressInfo(),
    var numeroContacto: Int = 0,
    var groupId: String = "",
    var admin: Boolean = false,
    var tarjetas: MutableList<Card.CardInfo> = mutableListOf(),
    var repartidor: String = "No"
)
