package ar.edu.utn.frba.foody.ui.Classes


data class OrderItemInfo(
    var id: String = "",
    val dish: Dish = Dish(),
    var quantity: Int = 0
)
