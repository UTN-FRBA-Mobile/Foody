package ar.edu.utn.frba.foody.ui.main

import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.UiString

class MainViewModel() : ViewModel() {
    var labelBotonIngresar: String? = null
    var labelBotonRegistrarse: String? = null
    var labelMail: String? = null
    var labelContrasena: String? = null

    val navegarRegistro get() = UiString.Resource(R.string.label_mail)
}
