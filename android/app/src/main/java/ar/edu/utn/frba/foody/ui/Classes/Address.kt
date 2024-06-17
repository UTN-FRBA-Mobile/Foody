package ar.edu.utn.frba.foody.ui.Classes


class Address {
    data class AddressInfo(
        val id: Int =0,
        val calle: String="",
        val numero: Int=0,
        val localidad: String="",
        val region: String="",
        val latitud: Double=0.0,
        val longitud: Double=0.0
    )
}