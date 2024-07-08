package ar.edu.utn.frba.foody.ui.Classes

data class User(
    var userId: String = "",
    var email: String = "",
    var password: String = "",
    var address: Address.AddressInfo = Address.AddressInfo(),
    var contactNumber: Int = 0,
    var groupId: String = "",
    var admin: Boolean = false,
    var cards: MutableList<Card.CardInfo> = mutableListOf(),
    var delivery: String = "No"
)
