package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.*

class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order.OrderInfo())

    fun updateOrder(newOrder: Order.OrderInfo) {
        order = newOrder
    }

    val orders: List<Order.OrderInfo> = listOf(
        Order.OrderInfo(
            orderId = 1,
            name = "Order 1",
            inProgress = false,
        ),
        Order.OrderInfo(
            orderId = 2,
            name = "Order 2",
            inProgress = true,
        ),
        Order.OrderInfo(
            orderId = 3,
            name = "Order 3",
            inProgress = false,
        )
    )
}