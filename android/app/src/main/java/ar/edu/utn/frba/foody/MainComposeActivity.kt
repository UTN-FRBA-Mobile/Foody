package ar.edu.utn.frba.foody

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataClasses.CardViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppNavigation

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = viewModel<MainViewModel>()
            val orderViewModel = viewModel<OrderViewModel>()
            val cardViewModel = viewModel<CardViewModel>()
            createTestData(orderViewModel)
            AppNavigation(viewModel, orderViewModel,cardViewModel, this)
        }
    }

    fun createTestData(viewModel: OrderViewModel){
        val dish1 = Dish.DishInfo(
            dishId = 1,
            name = "Spaghetti Carbonara",
            description = "Classic Italian pasta",
            imageResourceId = R.drawable.spaghetti_carbonara,
            price = 5000.0
        )

        val dish2 = Dish.DishInfo(
            dishId = 2,
            name = "Margherita Pizza",
            description = "Pizza with tomatoes, mozzarella, and basil",
            imageResourceId = R.drawable.peperoni_pizza,
            price = 8000.0
        )

        val restaurant = Restaurant.RestaurantInfo(
            restaurantId = 1,
            name = "La Bella Italia",
            imageDescription = "A cozy Italian restaurant",
            image = R.drawable.italian_restaurant,
            dishes = listOf(dish1, dish2)
        )

        val orderItems1 = listOf(OrderItemInfo(dish = dish1, quantity = 2, id = 1), OrderItemInfo(dish = dish2, quantity = 1, id = 2))
        val orderItems2 = listOf(OrderItemInfo(dish = dish2, quantity = 3, id = 2), OrderItemInfo(dish = dish1, quantity = 1, id = 3))
        val orderItems3 = listOf(OrderItemInfo(dish = dish2, quantity = 2, id = 3))
        val orderItems4 = listOf(OrderItemInfo(dish = dish1, quantity = 4, id = 4), OrderItemInfo(dish = dish2, quantity = 2, id = 1))
        val orderItems5 = listOf(OrderItemInfo(dish = dish1, quantity = 1, id = 5))

        val user1 = User(userId = 1, userName = "Alice")
        val user2 = User(userId = 2, userName = "Bob")
        val user3 = User(userId = 3, userName = "Charlie")
        val user4 = User(userId = 4, userName = "Dave")
        val user5 = User(userId = 5, userName = "Eve")

        val userOrder1 = UserOrder(userOrderId = 1, items = orderItems1, user = user1)
        val userOrder2 = UserOrder(userOrderId = 2, items = orderItems2, user = user2)
        val userOrder3 = UserOrder(userOrderId = 3, items = orderItems3, user = user3)
        val userOrder4 = UserOrder(userOrderId = 4, items = orderItems4, user = user4)
        val userOrder5 = UserOrder(userOrderId = 5, items = orderItems5, user = user5)

        val order1 = Order(orderId = 123, name = "Sample Order", restaurant = restaurant, userOrders = listOf(userOrder1, userOrder2, userOrder3, userOrder4, userOrder5))
        viewModel.updateOrder(order1)

        val order2 = Order(orderId = 222, name = "Order", restaurant = restaurant, userOrders = listOf(userOrder1, userOrder3, userOrder5))
        viewModel.updateOrder(order2)

        val order3 = Order(orderId = 321, name = "Super Order", restaurant = restaurant, userOrders = listOf(userOrder1, userOrder2, userOrder3, userOrder5))
        viewModel.updateOrder(order3)
    }
}

