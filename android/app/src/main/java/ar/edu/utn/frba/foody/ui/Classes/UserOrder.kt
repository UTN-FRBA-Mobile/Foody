package ar.edu.utn.frba.foody.ui.Classes

data class UserOrder(
    var userOrderId: String,
    var items: List<OrderItemInfo> = emptyList(),
    val user: User
)