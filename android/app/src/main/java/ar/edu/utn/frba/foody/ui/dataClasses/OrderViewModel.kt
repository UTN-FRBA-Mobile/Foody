package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.*
import java.util.Calendar

class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order())

    fun updateOrder(newOrder: Order) {
        order = newOrder
    }

    fun getPickedOrder(): Order {
        return order
    }

    val orders: List<Order> = listOf(
        Order(
            orderId = 1,
            name = "Order 1",
            inProgress = false,
            direction = "Dorrego 1352",
            estimatedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        ),
        Order(
            orderId = 2,
            name = "Order 2",
            inProgress = true,
            direction = "Suarez 3450",
            estimatedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        ),
        Order(
            orderId = 3,
            name = "Order 3",
            inProgress = false,
            direction = "Santa Fe 34",
            estimatedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        )
    )
}
