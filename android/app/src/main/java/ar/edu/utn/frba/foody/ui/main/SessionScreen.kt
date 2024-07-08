package ar.edu.utn.frba.foody.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.StoreUserSession.StoreUserSession
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: MainViewModel,
    restaurantDataBase: RestaurantDataBase?,
    orderViewModel: OrderViewModel,
    groupViewModel: GroupViewModel,
    intent: Intent
) {
    val context = LocalContext.current
    val dataStore = StoreUserSession(context)
    val userSession = dataStore.getSession.collectAsState(initial = "")

    AppScaffold () {
        if (userSession.value == "")
        {
            LoginScreen(
                navController = navController as NavHostController,
                mainViewModel = viewModel,
                orderViewModel = orderViewModel)
        }else{
            val notification = intent.getStringExtra("notification")
            if (notification != null) {
                orderViewModel.findAllOrdersByState()
                var order_id =orderViewModel.getAllOrdersByState().last().orderId
                navController.navigate(AppScreens.Progress_Order_Screen.createRoute(order_id))
            }
            else {
                HomeScreen(
                    navController = navController,
                    viewModel = viewModel,
                    restaurantDataBase = restaurantDataBase,
                    orderViewModel = orderViewModel,
                    groupViewModel = groupViewModel
                )
            }
        }
    }
}