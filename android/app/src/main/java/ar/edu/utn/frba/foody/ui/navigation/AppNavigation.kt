package ar.edu.utn.frba.foody.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.main.HomeScreen
import ar.edu.utn.frba.foody.ui.main.LoginScreen
import ar.edu.utn.frba.foody.ui.main.RestaurantScreen
import ar.edu.utn.frba.foody.ui.main.SignUpScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreens.Login_Screen.route) {
        composable(route = AppScreens.Home_Screen.route) {
            HomeScreen(navController = navController)
        }
        composable(route = AppScreens.Login_Screen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = AppScreens.SignUp_Screen.route){
            SignUpScreen(navController = navController)
        }
        composable(route = AppScreens.Profile_Screen.route) {

        }
        composable(route = AppScreens.Cart_Screen.route) {

        }
        composable(route = AppScreens.Order_Screen.route) {

        }
        composable(route = AppScreens.Restaurant_Screen.route) {
            RestaurantScreen(navController = navController)
        }
    }
}