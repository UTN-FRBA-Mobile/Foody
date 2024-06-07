package ar.edu.utn.frba.foody.ui.Classes

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AddressViewModel : ViewModel() {
    private val _calle = MutableLiveData<String>()
    val calle: LiveData<String> get() = _calle

    private val _nro = MutableLiveData<Int>()
    val nro: LiveData<Int> get() = _nro

    private val _latitud = MutableLiveData<Double>()
    val latitud: LiveData<Double> get() = _latitud

    private val _longitud = MutableLiveData<Double>()
    val longitud: LiveData<Double> get() = _longitud

    fun updateAddress(calle: String, nro: Int, latitud: Double, longitud: Double) {
        _calle.value = calle
        _nro.value = nro
        _latitud.value = latitud
        _longitud.value = longitud
    }
}