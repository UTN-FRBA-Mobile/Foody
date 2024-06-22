package ar.edu.utn.frba.foody.ui.Classes

data class OrderState (
    val resourceId: Int = 0,
    val imageDescription: String = "",
    val description: String = "",
    var completed: Boolean = false,
    val firstState: Boolean = false,
    var current: Boolean = false,
)