package ar.edu.utn.frba.foody.ui.Classes

data class Order(
    var orderId: Int = 0,
    val restaurant: Restaurant = Restaurant(),
    val name: String = "",
    val inProgress: Boolean = false,
    var userOrders: List<UserOrder> = emptyList(),
    val direction: String = "",
    val estimatedHour: String = "",
    val orderStates: List<OrderState> = emptyList(),
    var group: Group? = null,
)