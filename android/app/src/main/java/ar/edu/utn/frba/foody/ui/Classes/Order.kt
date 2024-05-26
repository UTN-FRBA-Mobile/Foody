package ar.edu.utn.frba.foody.ui.Classes

data class Order(
    val orderId: Int = 0,
    val restaurant: Restaurant.RestaurantInfo = Restaurant.RestaurantInfo(),
    val description: String = "",
    val userOrders: List<UserOrder> = emptyList()
)
