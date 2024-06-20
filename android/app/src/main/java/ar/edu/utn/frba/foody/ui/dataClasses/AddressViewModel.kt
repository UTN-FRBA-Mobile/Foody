package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.Address
import androidx.compose.runtime.State


class AddressViewModel : ViewModel() {

    private var _address = mutableStateOf(Address.AddressInfo())
    val address: State<Address.AddressInfo> get() = _address

    fun updateAddress(newAddress: Address.AddressInfo) {
        _address.value = newAddress
    }

    fun getPickedAddress(): Address.AddressInfo {
        return _address.value
    }
}