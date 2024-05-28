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

        var userOrder = order.userOrders[userOrderIndex]

        val userItemIndex = userOrder.items.indexOfFirst { it.id == userItemId }

        var userItem = userOrder.items[userItemIndex]

        if(variation < 0 && userItem.quantity == 0)
            return;

        val newQuantity = userItem.quantity + variation

        val updatedUserItem = userItem.copy(quantity = newQuantity)

        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems[userItemIndex] = updatedUserItem

        val updatedUserOrder = userOrder.copy(items = updatedUserItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toList())
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