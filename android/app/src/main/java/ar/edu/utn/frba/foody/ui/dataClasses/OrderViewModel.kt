package ar.edu.utn.frba.foody.ui.dataClasses

import ar.edu.utn.frba.foody.ui.Classes.Order
import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import java.util.Calendar
import ar.edu.utn.frba.foody.R

class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order())

    fun updateOrder(newOrder: Order) {
        order = newOrder
    }

    fun getPickedOrder(): Order {
        return order
    }

    val defaultOrderStates: List<OrderState> = listOf(
        OrderState(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            description = "Recibimos tu pedido",
            firstState = true
        ),
        OrderState(
            resourceId = R.drawable.store_icon,
            imageDescription = "Store Icon",
            description = "Estamos preparando tu pedido",
        ),
        OrderState(
            resourceId = R.drawable.delivery_icon,
            imageDescription = "Delivery Icon",
            description = "Tu pedido está en camino",
        ),
        OrderState(
            resourceId = R.drawable.finished_icon,
            imageDescription = "Finished Icon",
            description = "Entregamos tu pedido",
        ),
    )

    val orders: List<Order> = listOf(
        Order(
            orderId = 1,
            name = "Order 1",
            inProgress = false,
            direction = "Dorrego 1352",
            estimatedHour = getCurrentTime(),
            orderStates = defaultOrderStates
        ),
        Order(
            orderId = 2,
            name = "Order 2",
            inProgress = true,
            direction = "Suarez 3450",
            estimatedHour = getCurrentTime(),
            orderStates = defaultOrderStates
        ),
        Order(
            orderId = 3,
            name = "Order 3",
            inProgress = false,
            direction = "Santa Fe 34",
            estimatedHour = getCurrentTime(),
            orderStates = defaultOrderStates
        )
    )
}

@SuppressLint("DefaultLocale")
fun getCurrentTime(): String {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    return String.format("%2d:%02d", hour, minute)
}