package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.Address
import androidx.compose.runtime.State


class AddressViewModel : ViewModel() {

    private var _address = mutableStateOf(
        Address.AddressInfo(
            id = null,
            calle = null,
            numero = null,
            localidad = null,
            region = null,
            latitud = null,
            longitud = null
        )
    )

    fun updateAddress(newAddress: Address.AddressInfo) {
        _address.value = newAddress
    }

    fun getPickedAddress(): Address.AddressInfo {
        return _address.value
    }

    fun existAddress(): Boolean {
        return _address.value.calle != null && _address.value.numero != null && _address.value.localidad != null && _address.value.region != null
    }

    fun emptyAddress() {
        _address = mutableStateOf(
            Address.AddressInfo(
                id = null,
                calle = null,
                numero = null,
                localidad = null,
                region = null,
                latitud = null,
                longitud = null
            )
        )
    }

    fun isEmptyAddress(address: Address.AddressInfo): Boolean {
        return address.calle == "" || address.region == "" || address.localidad == "" || address.numero == 0 || address.latitud == 0.0 || address.longitud == 0.0
    }
}