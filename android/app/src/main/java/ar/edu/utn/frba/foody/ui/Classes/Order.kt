package ar.edu.utn.frba.foody.ui.Classes

import java.util.Calendar

data class Order(
    val orderId: Int = 0,
    val restaurant: Restaurant.RestaurantInfo = Restaurant.RestaurantInfo(),
    val name: String = "",
    val inProgress: Boolean = false,
    val userOrders: List<UserOrder> = emptyList(),
    val direction: String = "",
    val estimatedHour: Int = Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
)