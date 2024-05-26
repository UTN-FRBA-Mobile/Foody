package ar.edu.utn.frba.foody.ui.Classes

data class Order(
    val orderId: Int = 0,
    val restaurant: Restaurant.RestaurantInfo = Restaurant.RestaurantInfo(),
    val name: String = "",
    val inProgress: Boolean = false,
    val userOrders: List<UserOrder> = emptyList()
)