package ar.edu.utn.frba.foody.ui.Classes
class Address {
    data class AddressInfo(
        val street: String? = "",
        val number: Int? = 0,
        val location: String? = "",
        val country: String? = "",
        val latitude: Double? = 0.0,
        val longitude: Double? = 0.0
    )
}