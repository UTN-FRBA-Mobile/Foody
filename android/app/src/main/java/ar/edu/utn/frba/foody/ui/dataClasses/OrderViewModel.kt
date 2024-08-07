package ar.edu.utn.frba.foody.ui.dataClasses

import android.annotation.SuppressLint
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Status
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.OrderDataBaseFirebase
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import java.util.Calendar

class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order(""))
    private var orders by mutableStateOf(listOf<Order>())
    private var pendingOrders by mutableStateOf(listOf<Order>())
    private var orderByDeliveryMan by mutableStateOf(listOf<Order>())
    private var orderDetail by mutableStateOf(Order(""))
    var orderDataBaseFirebase: OrderDataBaseFirebase? = null
    var user by mutableStateOf(User(""))
    var navController: NavController? = null

    private val _addUserOrderResult = MutableLiveData<Boolean>()
    val addUserOrderResult: LiveData<Boolean> get() = _addUserOrderResult
    fun setServices(
        orderDataBaseFirebase: OrderDataBaseFirebase,
        navController: NavController
    ) {
        this.orderDataBaseFirebase = orderDataBaseFirebase
        this.navController = navController
    }
    fun updateOrderLogin() {
        this.findAllOrdersForUser()
        this.getOrderByState(Status.INPROGRESS)
    }
    fun getPendingsOrders(): List<Order> {
        return pendingOrders
    }
    fun updatePendingOrders(orders: List<Order>) {
        this.pendingOrders = orders
    }
    fun updateOrder(order: Order) {
        this.order = order
    }

    fun updateUser(user: User) {
        this.user = user
    }

    fun updateDataBaseOrder(newOrder: Order) {
        this.updateOrder(newOrder)
        orderDataBaseFirebase?.updateOrder(order) {}
    }
    fun createOrderWithStates(address: String, totalPayment: Double, card: String): Order {
        return order.copy(
            status = Status.PENDING,
            address = address,
            amount = totalPayment,
            card = card,
            orderStates = listOf(
                OrderState(
                    R.drawable.order_icon,
                    "Order Icon",
                    "Hemos tomado tu pedido",
                    true,
                    true,
                    true
                ),
                OrderState(
                    R.drawable.delivery_icon,
                    "Delivery Icon",
                    "Tu pedido está en camino",
                    false,
                    false,
                    false
                ),
                OrderState(
                    R.drawable.finished_icon,
                    "Finished Icon",
                    "Entregamos tu pedido",
                    false,
                    false,
                    false
                ),
            )
        )
    }
    fun getPickedOrder(): Order {
        return order
    }
    fun getAllOrdersForUser(): List<Order> {
        return orders
    }
    fun createGroup(newGroup: Group) {
        user.admin = true
        this.updateGroup(newGroup)
    }
    fun updateGroup(newGroup: Group) {
        order = order.copy(group = newGroup)
        orderDataBaseFirebase?.updateOrder(order) {}
    }
    fun getOrderByGroup(groupId: String, callback: (Order?) -> Unit) {
        orderDataBaseFirebase?.getOrderByGroup(groupId) { order ->
            if (order != null) {
                this.updateOrder(order)
                callback(order)
            }
        }
    }
    fun addUser() {
        val updatedMembers = order.group?.members?.toMutableList()
        updatedMembers?.add(user)
        val updatedOrder =
            order.copy(group = updatedMembers?.let { order.group?.copy(members = it.toList()) })
        orderDataBaseFirebase?.updateOrder(updatedOrder) {
            this.updateOrder(updatedOrder)
        }
    }
    fun hasItems(newOrder: Order): Boolean {
        return newOrder.userOrders.any { userOrder -> userOrder.items.isNotEmpty() }
    }
    fun getUserOrder(restaurant: Restaurant): UserOrder {
        if (order.orderId == "") {
            createOrder(restaurant)
        }
        return getAssignedUserOrder()
    }
    fun getAssignedUserOrder(): UserOrder {
        val userOrder = order.userOrders.firstOrNull() { x -> x.user.userId == this.user.userId }
        if (userOrder == null) {
            return updateUserOrder(order)
        }
        return userOrder
    }
    fun removeOrderFromSession() {
        order = Order("")
    }
    fun createOrder(restaurant: Restaurant) {
        val createdOrder = Order(
            "",
            restaurant,
            restaurant.name,
            user.address.street + " " + user.address.number
        )
        createdOrder.orderId = orderDataBaseFirebase?.addOrder(createdOrder) ?: ""
        val loading = mutableStateOf(false)
        createUserOrder(createdOrder, loading)
    }
    fun createUserOrder(newOrder: Order, loading: MutableState<Boolean>): UserOrder {
        val userOrder = UserOrder("", mutableListOf(), user)
        orderDataBaseFirebase?.addUserOrderToOrder(newOrder.orderId, userOrder) { isSuccess ->
            _addUserOrderResult.postValue(isSuccess)
            if (isSuccess) {
                loading.value = false
            }
        }
        loading.value = true
        val userOrders = mutableListOf<UserOrder>()
        userOrders.add(userOrder)
        this.order = newOrder.copy(userOrders = userOrders)
        return userOrder
    }
    fun updateUserOrder(newOrder: Order): UserOrder {
        val userOrder = UserOrder("", mutableListOf(), user)
        val updatedUserOrders = newOrder.userOrders.toMutableList()
        updatedUserOrders.add(userOrder)
        order = newOrder.copy(userOrders = updatedUserOrders)
        orderDataBaseFirebase?.updateOrder(order) { }
        return userOrder
    }
    fun getTotal(): Double {
        return order.userOrders.sumOf { x -> x.items.sumOf { y -> y.quantity * y.dish.price } }
    }
    fun deleteItem(dishId: Int, userId: String) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == userId }
        val userOrder = order.userOrders[userOrderIndex]
        if (userOrder.items.size > 1) {
            val updatedItems = userOrder.items.filter { it.dish.dishId != dishId }
            val updatedUserOrder = userOrder.copy(items = updatedItems.toMutableList())
            val updatedUserOrders = order.userOrders.toMutableList()
            updatedUserOrders[userOrderIndex] = updatedUserOrder
            order = order.copy(userOrders = updatedUserOrders.toMutableList())
            orderDataBaseFirebase?.updateUserOrder(order.orderId,
                updatedUserOrder) {}
        } else {
            this.emptyUserOrder(userId)
        }
    }
    fun changeItemQuantity(dishId: Int, variation: Int, userId: String) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == userId }
        val userOrder = order.userOrders[userOrderIndex]
        val userItemIndex = userOrder.items.indexOfFirst { it.dish.dishId == dishId }
        val userItem = userOrder.items[userItemIndex]
        if (userItem.quantity + variation == 0) {
            deleteItem(dishId, userId)
            return
        }
        val newQuantity = userItem.quantity + variation
        val updatedUserItem = userItem.copy(quantity = newQuantity)
        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems[userItemIndex] = updatedUserItem
        val updatedUserOrder = userOrder.copy(items = updatedUserItems)
        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder
        order = order.copy(userOrders = updatedUserOrders.toMutableList())
        orderDataBaseFirebase?.updateUserOrder(order.orderId, updatedUserOrder) {}
    }
    fun addItem(quantity: Int, dish: Dish) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == user.userId }
        val userOrder = order.userOrders[userOrderIndex]
        val newOrderItem = OrderItemInfo("", dish, quantity)
        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems.add(newOrderItem)
        val updatedUserOrder = userOrder.copy(items = updatedUserItems)
        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder
        order = order.copy(userOrders = updatedUserOrders.toMutableList())
        orderDataBaseFirebase?.updateUserOrder(order.orderId, updatedUserOrder) {}
    }
    fun changeRestaurant(newRestaurant: Restaurant) {
        orderDataBaseFirebase?.getOrderByState(Status.INPROGRESS, user) { orderReceived ->
            if (orderReceived != null) {
                orderDataBaseFirebase?.deleteOrder(orderReceived.orderId) {}
            }
        }
        createOrder(newRestaurant)
    }
    fun changeItemQuantityIfExists(
        orderItem: OrderItemInfo?,
        variation: Int,
        dish: Dish,
        userId: String
    ) {
        if (orderItem == null) {
            if (variation > 0) {
                addItem(variation, dish)
            }
        } else {
            changeItemQuantity(dish.dishId, variation, userId)
        }
    }
    fun getOrder() {
        orderDataBaseFirebase?.getOrderById(order.orderId) { order ->
            if (order != null) {
                this.order = order
            }
        }
    }
    fun findAllOrdersForUser() {
        orderDataBaseFirebase?.getOrdersByUser(user.userId) { orders ->
            if (orders.isNotEmpty()) {
                this.orders = orders
            }
        }
    }
    fun getOrderById(order_id: String): Order {
        orderDataBaseFirebase?.getOrderById(order_id) { order ->
            if (order != null) {
                this.orderDetail = order
            }
        }
        return orderDetail
    }
    fun getOrderByState(status: Status) {
        orderDataBaseFirebase?.getOrderByState(status, user) { order ->
            if (order != null) {
                this.order = order
            }
        }
    }
    fun findAllOrdersByState() {
        orderDataBaseFirebase?.getOrdersByState() { orders ->
            if (orders.isNotEmpty()) {
                this.pendingOrders = orders.toMutableList()
            }
        }
    }

    fun findAllOrdersByState2() {
        orderDataBaseFirebase?.getOrdersByState() { orders ->
            if (orders.isNotEmpty()) {
                this.pendingOrders = orders.toMutableList()
                var order_id = pendingOrders.last().orderId
                navController!!.navigate(AppScreens.Progress_Order_Screen.createRoute(order_id))
            }
        }
    }

    fun findOrdersDeliveredById() {
        orderDataBaseFirebase?.getOrdersByDeliveredId(user.userId) { orders ->
            if (orders.isNotEmpty()) {
                this.orderByDeliveryMan = orders.toMutableList()
            }
        }
    }

    fun getAllOrdersDeliveredById(): List<Order> {
        return orderByDeliveryMan
    }

    fun getAllOrdersByState(): List<Order> {
        return pendingOrders
    }

    fun emptyUserOrder(userId: String) {
        var userIdToEmpty = userId

        if(userId == "")
            userIdToEmpty = user.userId

        val userOrders = order.userOrders.filter { x -> x.user.userId != userId }
        order = order.copy(userOrders = userOrders.toMutableList())

        orderDataBaseFirebase?.updateUserOrderList(
            order.orderId,
            mutableListOf<UserOrder>()
        ) {}
    }

    fun createOrderGroup(group: Group, restaurant: Restaurant): Order {
        this.deleteCurrentOrder()

        val createdOrder = Order(orderId = "", group = group, restaurant = restaurant)
        createdOrder.orderId = orderDataBaseFirebase?.addOrder(createdOrder) ?: ""

        val loading = mutableStateOf(false)
        createUserOrder(createdOrder, loading)

        return order
    }

    fun deleteCurrentOrder() {
        orderDataBaseFirebase?.getOrderByState(Status.INPROGRESS, user) { orderInProgress ->
            if (orderInProgress != null) {
                orderDataBaseFirebase?.deleteOrder(orderInProgress.orderId) {}
            }
        }
    }

    fun enableChangeUserOrderButton(userId: String): Boolean {
        return isAdmin() || user.userId == userId
    }

    fun enablePayOrder(): Boolean {
        return isAdmin() && orderIsNotEmpty()
    }

    fun isAdmin(): Boolean {
        return order.group == null || user.admin
    }

    fun orderIsNotEmpty(): Boolean {
        return order.userOrders.any {
            x -> x.items.any {
                y -> y.quantity > 0
            }
        }
    }

    fun updateAddress(newAddress: Address.AddressInfo) {
        user.address = newAddress
    }

    fun existAddress(): Boolean {
        return user.address.street != null
                && user.address.number != null
                && user.address.location != null
                && user.address.country != null
    }

    fun emptyAddress() {
        user.address = Address.AddressInfo(
            street = null,
            number = null,
            location = null,
            country = null,
            latitude = null,
            longitude = null
        )
    }

    fun isEmptyAddress(address: Address.AddressInfo): Boolean {
        return address.street == ""
                || address.country == ""
                || address.location == ""
                || address.number == 0
                || address.latitude == 0.0
                || address.longitude == 0.0
    }

    fun updateUserOrders(user: User) {
        val updatedUserOrders =
            order.userOrders.filter { it.user.userId != user.userId }
        val updatedOrder = order.copy(userOrders = updatedUserOrders.toMutableList())
        this.removeOrderFromSession()

        orderDataBaseFirebase?.updateOrder(updatedOrder) {}
    }

    fun reassignAdmin(newGroup: Group): Group {
        var adminFound = false

        newGroup.members.forEach { member ->
            if (!member.admin && !adminFound) {
                member.admin = true
                adminFound = true
            }
        }

        return newGroup
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
}

@SuppressLint("DefaultLocale")
fun getCurrentTime(): String {
    val cal = Calendar.getInstance()
    val hour = cal.get(Calendar.HOUR_OF_DAY)
    val minute = cal.get(Calendar.MINUTE)
    return String.format("%2d:%02d", hour, minute)
}