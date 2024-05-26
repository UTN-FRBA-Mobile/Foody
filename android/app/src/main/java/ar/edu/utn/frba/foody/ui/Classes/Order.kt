package ar.edu.utn.frba.foody.ui.Classes

class Order {
    data class OrderInfo(
        val orderId: Int = 0,
        val name: String = "",
        val inProgress: Boolean = false,
    )
}