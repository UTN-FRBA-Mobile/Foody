package ar.edu.utn.frba.foody.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import ar.edu.utn.frba.foody.ui.dataClasses.*
import ar.edu.utn.frba.foody.ui.main.*

@Composable
fun AppNavigation(viewModel: MainViewModel, orderViewModel: OrderViewModel){
    val navController= rememberNavController()
    NavHost(navController = navController , startDestination = AppScreens.Login_Screen.route) {
        composable(route = AppScreens.Home_Screen.route){
            HomeScreen(navController = navController, viewModel = viewModel )
        }
        composable(route = AppScreens.Login_Screen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreens.SignUp_Screen.route){
            SignUpScreen(navController = navController)
        }
        composable(route = AppScreens.Profile_Screen.route) {

        }

        composable(route = AppScreens.Cart_Screen.route){
            CartScreen(navController = navController, viewModel = orderViewModel)
        }
        composable(route = AppScreens.Orders_Screen.route){
            OrdersScreen(navController = navController, orderViewModel)
        }
        composable(route = AppScreens.Restaurant_Screen.route){
            RestaurantScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AppScreens.Order_Screen.route) {

        }
        composable(route = AppScreens.Progress_Order_Screen.route){
            ProgressOrderScreen(navController = navController, orderViewModel = orderViewModel)
        }
        composable(route = AppScreens.Group_Screen.route) {

        }
        composable(route = AppScreens.Create_Group_Screen.route) {
            CreateGroupScreen(navController = navController, orderViewModel = orderViewModel)
        }
    }
}