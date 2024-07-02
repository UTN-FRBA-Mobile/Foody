package ar.edu.utn.frba.foody.ui.Classes

data class Order(
    var orderId: String = "",
    val restaurant: Restaurant = Restaurant(),
    val name: String = "",
    var direction: String = "",
    var estado: Estado = Estado.ENPROGRESO,
    var userOrders: MutableList<UserOrder> = mutableListOf(),
    val estimatedHour: String = "",
    val orderStates: List<OrderState> = emptyList(),
    var group: Group? = null,
    var montoPagado: Double = 0.0,
    var tarjetaUsada: String = "",
    var repartidor: User? = null
)