package ar.edu.utn.frba.foody.ui.dataClasses

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.OrderDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.OrderDataBase
import java.util.Calendar


class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order(""))

    var orderDataBase: OrderDataBase? = null
    var orderDataBaseFirebase: OrderDataBaseFirebase? = null

     var user = User()
         set(value) {
             field = value
         }

    private val _addUserOrderResult = MutableLiveData<Boolean>()
    val addUserOrderResult: LiveData<Boolean> get() = _addUserOrderResult


    fun setDatabase(orderDataBase: OrderDataBase, orderDataBaseFirebase: OrderDataBaseFirebase) {
        this.orderDataBase = orderDataBase
        this.orderDataBaseFirebase = orderDataBaseFirebase
    }

    fun updateOrder(newOrder: Order) {
        order = newOrder
    }

    fun getPickedOrder(): Order {
        return order
    }

    fun createGroup(newGroup: Group) {
        user.admin = true
        this.updateGroup(newGroup)
    }

    fun updateGroup(newGroup: Group) {
        order = order.copy(group = newGroup)
        orderDataBase?.updateGroup(newGroup.groupId, order.orderId)
    }

    fun hasItems(newOrder: Order): Boolean {
        return newOrder.userOrders.any { userOrder -> userOrder.items.isNotEmpty() }
    }

    fun getUserOrder(restaurant: Restaurant): UserOrder {
        if(order.orderId == ""){
            createOrder(restaurant)
        }
        return getAssignedUserOrder()
    }

    fun getAssignedUserOrder(): UserOrder {
        val userOrder = order.userOrders.firstOrNull() { x -> x.user.userId == this.user.userId }
        if(userOrder == null) {
            return createUserOrder(order)
        }
        return userOrder
    }

    fun removeOrderFromSession() {
        order = Order("")
    }

    fun createOrder(restaurant: Restaurant) {
        val createdOrder = Order("", restaurant)

        createdOrder.orderId  = orderDataBaseFirebase?.addOrder(createdOrder) ?: ""

        createUserOrder(createdOrder)
    }

    fun createUserOrder(newOrder: Order): UserOrder {
        val userOrder = UserOrder("", mutableListOf(), user)

        orderDataBaseFirebase?.addUserOrderToOrder(newOrder.orderId, userOrder) { isSuccess ->
            _addUserOrderResult.postValue(isSuccess)
            //TODO: desbloquear pantalla
        }

        //TODO: bloquear pantalla

        val userOrders = mutableListOf<UserOrder>()

        userOrders.add(userOrder)

        this.order = newOrder.copy(userOrders = userOrders)

        return userOrder
    }

    fun getTotal(): Double {
        return order.userOrders.sumOf { x -> x.items.sumOf { y -> y.quantity * y.dish.price } }
    }

    fun deleteItem(userOrderId: String, userItemId: String) {
        orderDataBase?.deleteOrderItem(userOrderId)

        val userOrderIndex = order.userOrders.indexOfFirst { it.userOrderId == userOrderId }

        val userOrder = order.userOrders[userOrderIndex]

        val updatedItems = userOrder.items.filter { it.id != userItemId }

        val updatedUserOrder = userOrder.copy(items = updatedItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toMutableList())
    }

    fun changeItemQuantity(userOrderId: String, userItemId: String, variation: Int) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.userOrderId == userOrderId }

        val userOrder = order.userOrders[userOrderIndex]

        val userItemIndex = userOrder.items.indexOfFirst { it.id == userItemId }

        val userItem = userOrder.items[userItemIndex]

        if(variation < 0 && userItem.quantity == 0)
            return

        val newQuantity = userItem.quantity + variation

        orderDataBase?.updateQuantityOrderItem(orderItemId = userItemId, newQuantity = newQuantity)

        val updatedUserItem = userItem.copy(quantity = newQuantity)

        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems[userItemIndex] = updatedUserItem

        val updatedUserOrder = userOrder.copy(items = updatedUserItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toMutableList())
    }

    fun addItem(userOrderId: String, quantity: Int, dish: Dish) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == user.userId }

        val userOrder = order.userOrders[userOrderIndex]

        var newOrderItem = OrderItemInfo("", dish, quantity)

        val orderItemId = orderDataBase?.insertOrderItem(newOrderItem, userOrderId) ?: ""

        newOrderItem.id = orderItemId

        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems.add(newOrderItem)

        val updatedUserOrder = userOrder.copy(items = updatedUserItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toMutableList())
    }

    fun changeItemQuantityIfExists(userOrderId: String, orderItem: OrderItemInfo?, variation: Int, dish: Dish, restaurant: Restaurant) {
        if(orderItem == null && order.restaurant.restaurantId != restaurant.restaurantId) {
            createOrder(restaurant)
        }
        if (orderItem == null) {
            if(variation > 0) {
                addItem(userOrderId, variation, dish)
            }
        }
        else {
            changeItemQuantity(userOrderId, orderItem.id, variation)
        }
    }

    fun emptyUserOrder() {
        val userOrder = getAssignedUserOrder()
        userOrder.items.forEach {
            orderDataBase?.deleteOrderItem(it.id)
        }
        orderDataBase?.deleteUserOrder(userOrder.userOrderId)

        val userOrders = order.userOrders.filter { x -> x.user != user }
        order = order.copy(userOrders = userOrders.toMutableList())
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
            orderId = "1",
            name = "Order 1",
            inProgress = false,
            direction = "Dorrego 1352",
            estimatedHour = getCurrentTime(),
            orderStates = defaultOrderStates,
        ),
        Order(
            orderId = "2",
            name = "Order 2",
            inProgress = true,
            direction = "Suarez 3450",
            estimatedHour = getCurrentTime(),
            orderStates = defaultOrderStates
        ),
        Order(
            orderId = "3",
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