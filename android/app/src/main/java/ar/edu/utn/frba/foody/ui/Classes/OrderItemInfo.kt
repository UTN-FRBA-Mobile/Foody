package ar.edu.utn.frba.foody.ui.Classes


data class OrderItemInfo(
    val id: Int,
    val dish: Dish,
    var quantity: Int
)
