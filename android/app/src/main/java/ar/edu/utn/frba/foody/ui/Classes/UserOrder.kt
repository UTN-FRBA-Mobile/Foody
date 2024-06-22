package ar.edu.utn.frba.foody.ui.Classes

data class UserOrder(
    var userOrderId: String = "",
    var items: MutableList<OrderItemInfo> = mutableListOf(),
    val user: User = User()
)