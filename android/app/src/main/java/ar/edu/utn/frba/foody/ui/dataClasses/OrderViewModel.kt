package ar.edu.utn.frba.foody.ui.dataClasses

import android.annotation.SuppressLint
import android.util.Log
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
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.OrderDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.OrderDataBase
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import java.util.Calendar


class OrderViewModel() : ViewModel() {
    private var order by mutableStateOf(Order(""))
    private var orders by mutableStateOf(listOf<Order>())

    var orderDataBase: OrderDataBase? = null
    var orderDataBaseFirebase: OrderDataBaseFirebase? = null
    var navController: NavController? = null

     var user = User()
         set(value) {
             field = value
         }

    private val _addUserOrderResult = MutableLiveData<Boolean>()
    val addUserOrderResult: LiveData<Boolean> get() = _addUserOrderResult


    fun setServices(orderDataBase: OrderDataBase, orderDataBaseFirebase: OrderDataBaseFirebase, navController: NavController) {
        this.orderDataBase = orderDataBase
        this.orderDataBaseFirebase = orderDataBaseFirebase
        this.navController = navController
    }

    fun updateOrder(newOrder: Order) {
        order = newOrder
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
        orderDataBase?.updateGroup(newGroup.groupId, order.orderId)
    }

    fun hasItems(newOrder: Order): Boolean {
        return newOrder.userOrders.any { userOrder -> userOrder.items.isNotEmpty() }
    }

    fun getUserOrder(restaurant: Restaurant, loading: MutableState<Boolean>): UserOrder {
        if(order.orderId == ""){
            createOrder(restaurant)
        }
        return getAssignedUserOrder(loading)
    }

    fun getAssignedUserOrder(loading: MutableState<Boolean>): UserOrder {

        val userOrder = order.userOrders.firstOrNull() { x -> x.user.userId == this.user.userId }
        if(userOrder == null) {
            return createUserOrder(order, loading)
        }
        return userOrder
    }

    fun removeOrderFromSession() {
        order = Order("")
    }

    fun createOrder(restaurant: Restaurant) {
        val createdOrder = Order("", restaurant, restaurant.name, user.direccion.calle + " " + user.direccion.numero)

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

    fun getTotal(): Double {
        return order.userOrders.sumOf { x -> x.items.sumOf { y -> y.quantity * y.dish.price } }
    }

    fun deleteItem(dishId: Int) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == user.userId }

        val userOrder = order.userOrders[userOrderIndex]

        if(userOrder.items.size > 1) {
            val updatedItems = userOrder.items.filter { it.dish.dishId != dishId }

            val updatedUserOrder = userOrder.copy(items = updatedItems.toMutableList())

            val updatedUserOrders = order.userOrders.toMutableList()
            updatedUserOrders[userOrderIndex] = updatedUserOrder

            order = order.copy(userOrders = updatedUserOrders.toMutableList())

            orderDataBaseFirebase?.updateUserOrder(order.orderId, updatedUserOrder) { isSuccess -> }
        }
        else {
            this.emptyUserOrder()
        }

    }

    fun changeItemQuantity(dishId: Int, variation: Int) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == user.userId }

        val userOrder = order.userOrders[userOrderIndex]

        val userItemIndex = userOrder.items.indexOfFirst { it.dish.dishId == dishId }

        val userItem = userOrder.items[userItemIndex]

        if(userItem.quantity + variation == 0) {
            deleteItem(dishId)
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

        //TODO: bloquear pantalla

        orderDataBaseFirebase?.updateUserOrder(order.orderId, updatedUserOrder) { isSuccess ->
            //TODO: desbloquear pantalla
        }
    }

    fun addItem(quantity: Int, dish: Dish) {
        val userOrderIndex = order.userOrders.indexOfFirst { it.user.userId == user.userId }

        val userOrder = order.userOrders[userOrderIndex]

        var newOrderItem = OrderItemInfo("", dish, quantity)

        val updatedUserItems = userOrder.items.toMutableList()
        updatedUserItems.add(newOrderItem)

        val updatedUserOrder = userOrder.copy(items = updatedUserItems)

        val updatedUserOrders = order.userOrders.toMutableList()
        updatedUserOrders[userOrderIndex] = updatedUserOrder

        order = order.copy(userOrders = updatedUserOrders.toMutableList())

        //TODO: bloquear pantalla

        orderDataBaseFirebase?.updateUserOrder(order.orderId, updatedUserOrder) { isSuccess ->
            //TODO: desbloquear pantalla
        }
    }

    fun changeRestaurant(newRestaurant: Restaurant) {
        orderDataBaseFirebase?.deleteOrder(order.orderId) { isSuccess ->
            createOrder(newRestaurant)
        }
    }

    fun changeItemQuantityIfExists(orderItem: OrderItemInfo?, variation: Int, dish: Dish, restaurant: Restaurant) {
        if (orderItem == null) {
            if(variation > 0) {
                addItem(variation, dish)
            }
        }
        else {
            changeItemQuantity(dish.dishId, variation)
        }
    }

    fun getOrder() {
        orderDataBaseFirebase?.getOrderById(order.orderId) { order ->
            if(order != null) {
                this.order = order
            }
        }
    }

    fun findAllOrdersForUser() {
        orderDataBaseFirebase?.getOrdersByUser(user.userId) { orders ->
            if(orders.isNotEmpty()) {
                this.orders = orders
            }
        }
    }

    fun emptyUserOrder() {
        val loading = mutableStateOf(false)
        val userOrder = getAssignedUserOrder(loading)

        val userOrders = order.userOrders.filter { x -> x.user != user }
        order = order.copy(userOrders = userOrders.toMutableList())

        orderDataBaseFirebase?.updateUserOrderList(order.orderId, mutableListOf<UserOrder>()) { isSuccess -> }
    }

    fun updateAddress(newAddress: Address.AddressInfo) {
        user.direccion = newAddress
    }

    fun existAddress(): Boolean {
        return user.direccion.calle != null
                && user.direccion.numero != null
                && user.direccion.localidad != null
                && user.direccion.region != null
    }

    fun emptyAddress() {
        user.direccion = Address.AddressInfo(
            calle = null,
            numero = null,
            localidad = null,
            region = null,
            latitud = null,
            longitud = null
        )
    }

    fun isEmptyAddress(address: Address.AddressInfo): Boolean {
        return address.calle == ""
                || address.region == ""
                || address.localidad == ""
                || address.numero == 0
                || address.latitud == 0.0
                || address.longitud == 0.0
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