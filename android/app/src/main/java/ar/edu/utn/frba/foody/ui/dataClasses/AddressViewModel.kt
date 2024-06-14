package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.Address

class AddressViewModel : ViewModel() {

    private var address by mutableStateOf(Address.AddressInfo())
        private set

    fun updateAddress(newAddress: Address.AddressInfo) {
        address = newAddress
    }

    fun getPickedAddress(): Address.AddressInfo {
        return address
    }
}