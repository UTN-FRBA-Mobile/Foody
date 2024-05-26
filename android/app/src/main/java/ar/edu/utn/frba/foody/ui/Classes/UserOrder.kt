package ar.edu.utn.frba.foody.ui.Classes

data class UserOrder(
    val userOrderId: Int,
    val items: List<OrderItemInfo> = emptyList(),
    val user: User
)