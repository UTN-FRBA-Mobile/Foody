package ar.edu.utn.frba.foody.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.main.HomeScreen
import ar.edu.utn.frba.foody.ui.main.LoginScreen
import ar.edu.utn.frba.foody.ui.main.OrdersScreen
import ar.edu.utn.frba.foody.ui.main.RestaurantScreen

@Composable
fun AppNavigation(viewModel: MainViewModel, orderViewModel: OrderViewModel){
    val navController= rememberNavController()
    NavHost(navController = navController , startDestination = AppScreens.Login_Screen.route) {
        composable(route = AppScreens.Home_Screen.route){
            HomeScreen(navController = navController, viewModel = viewModel )
        }
        composable(route = AppScreens.Login_Screen.route){
            LoginScreen(navController = navController)
        }
        composable(route = AppScreens.Profile_Screen.route){

        }
        composable(route = AppScreens.Cart_Screen.route){

        }
        composable(route = AppScreens.Orders_Screen.route){
            OrdersScreen(navController = navController, orderViewModel)
        }
        composable(route = AppScreens.Restaurant_Screen.route){
            RestaurantScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AppScreens.Order_Screen.route){

        }
        composable(route = AppScreens.Progress_Order_Screen.route){

        }
    }
}