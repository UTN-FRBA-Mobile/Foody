package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.Order

class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order())

    fun updateOrder(newOrder: Order) {
        order = newOrder
    }

    fun getPickedOrder(): Order {
        return order
    }

    fun getTotal(): Double {
        return 100.0;
    }

    val orders: List<Order> = listOf(
        Order(
            orderId = 1,
            name = "Order 1",
            inProgress = false,
        ),
        Order(
            orderId = 2,
            name = "Order 2",
            inProgress = true,
        ),
        Order(
            orderId = 3,
            name = "Order 3",
            inProgress = false,
        )
    )
}