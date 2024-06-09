package ar.edu.utn.frba.foody.ui.dataClasses

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import java.util.Calendar


class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order())
    private var userId by mutableStateOf(Int)

    fun updateOrder(newOrder: Order) {
        order = newOrder
    }

    fun getPickedOrder(): Order {
        return order
    }

    fun updateGroup(newGroup: Group) {
        order = order.copy(group = newGroup)
    }

    fun getUserOrder(): UserOrder {
        return order.userOrders.first { x -> x.user.userId == 1 }  //TODO CAMBIAR POR EL ID DEL USUARIO EN LA SESIÓN
    }

    fun getTotal(): Double {
        return order.userOrders.sumOf { x -> x.items.sumOf { y -> y.quantity * y.dish.price } }
    }

    fun deleteItem(userOrderId: Int, userItemId: Int) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.userOrderId == userOrderId }

        val userOrder = order.userOrders[userOrderIndex]

        val updatedItems = userOrder.items.filter { it.id != userItemId }

        val updatedUserOrder = userOrder.copy(items = updatedItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toList())
    }

    fun changeItemQuantity(userOrderId: Int, userItemId: Int, variation: Int) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.userOrderId == userOrderId }

        val userOrder = order.userOrders[userOrderIndex]

        val userItemIndex = userOrder.items.indexOfFirst { it.id == userItemId }

        val userItem = userOrder.items[userItemIndex]

        if(variation < 0 && userItem.quantity == 0)
            return

        val newQuantity = userItem.quantity + variation

        val updatedUserItem = userItem.copy(quantity = newQuantity)

        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems[userItemIndex] = updatedUserItem

        val updatedUserOrder = userOrder.copy(items = updatedUserItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toList())
    }

    fun addItem(userOrderId: Int, quantity: Int, dish: Dish) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.userOrderId == userOrderId }

        val userOrder = order.userOrders[userOrderIndex]

        val newOrderItem = OrderItemInfo(12, dish, quantity)  //TODO el id debe generarse solo

        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems.add(newOrderItem)

        val updatedUserOrder = userOrder.copy(items = updatedUserItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toList())
    }

    fun changeItemQuantityIfExists(userOrderId: Int, orderItem: OrderItemInfo?, variation: Int, dish: Dish) {
        if (orderItem == null) {
            if(variation > 0) {
                addItem(userOrderId, variation, dish)
            }
        }
        else {
            changeItemQuantity(userOrderId, orderItem.id, variation)
        }
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
            orderStates = defaultOrderStates,
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