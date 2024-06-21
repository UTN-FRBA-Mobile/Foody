package ar.edu.utn.frba.foody.ui.Classes


class Address {
    data class AddressInfo(
        var id: Int?,
        val calle: String?,
        val numero: Int?,
        val localidad: String?,
        val region: String?,
        val latitud: Double?,
        val longitud: Double?
    )
}