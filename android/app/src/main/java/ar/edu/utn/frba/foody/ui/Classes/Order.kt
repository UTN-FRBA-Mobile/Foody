package ar.edu.utn.frba.foody.ui.Classes

data class Order(
    var orderId: String = "",
    val restaurant: Restaurant = Restaurant(),
    val name: String = "",
    var address: String = "",
    var status: Status = Status.INPROGRESS,
    var userOrders: MutableList<UserOrder> = mutableListOf(),
    val estimatedHour: String = "",
    val orderStates: List<OrderState> = emptyList(),
    var group: Group? = null,
    var amount: Double = 0.0,
    var card: String = "",
    var delivery: User? = null
)