package ar.edu.utn.frba.foody.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.dataBase.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.UserDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.CardViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.main.CardInfoScreen
import ar.edu.utn.frba.foody.ui.main.CartScreen
import ar.edu.utn.frba.foody.ui.main.CreateGroupScreen
import ar.edu.utn.frba.foody.ui.main.GroupScreen
import ar.edu.utn.frba.foody.ui.main.HomeScreen
import ar.edu.utn.frba.foody.ui.main.LocationScreen
import ar.edu.utn.frba.foody.ui.main.LoginScreen
import ar.edu.utn.frba.foody.ui.main.OrdersScreen
import ar.edu.utn.frba.foody.ui.main.PaymentScreen
import ar.edu.utn.frba.foody.ui.main.ProgressOrderScreen
import ar.edu.utn.frba.foody.ui.main.RestaurantScreen
import ar.edu.utn.frba.foody.ui.main.SignUpScreen

@Composable
fun AppNavigation(viewModel: MainViewModel, orderViewModel: OrderViewModel, cardViewModel: CardViewModel,
                  groupViewModel: GroupViewModel,
                  dbUserHelper:UserDataBase, dbRestaurantHelper: RestaurantDataBase){
    val navController= rememberNavController()
    NavHost(navController = navController , startDestination = AppScreens.Login_Screen.route) {
        composable(route = AppScreens.Home_Screen.route){
            HomeScreen(navController = navController, viewModel = viewModel,
                restaurantDataBase = dbRestaurantHelper, userDataBase = dbUserHelper )
        }
        composable(route = AppScreens.Login_Screen.route) {
            LoginScreen(navController = navController,dbHelper=dbUserHelper)
        }
        composable(route = AppScreens.SignUp_Screen.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = AppScreens.Location_Screen.route) {
            LocationScreen(navController = navController)
        }
        composable(route = AppScreens.Profile_Screen.route) {

        }
        composable(route = AppScreens.Cart_Screen.route) {
            CartScreen(navController = navController, viewModel = orderViewModel, groupViewModel = groupViewModel)
        }
        composable(route = AppScreens.Orders_Screen.route) {
            OrdersScreen(navController = navController, orderViewModel)
        }
        composable(route = AppScreens.Restaurant_Screen.route) {
            RestaurantScreen(navController = navController, viewModel = viewModel, orderViewModel = orderViewModel)
        }
        composable(route = AppScreens.Order_Screen.route) {

        }
        composable(route = AppScreens.Progress_Order_Screen.route) {
            ProgressOrderScreen(navController = navController, orderViewModel = orderViewModel)
        }
        composable(route = AppScreens.Group_Screen.route) {
            GroupScreen(navController = navController, orderViewModel = orderViewModel, groupViewModel = groupViewModel)
        }
        composable(route = AppScreens.Create_Group_Screen.route) {
            CreateGroupScreen(navController = navController, orderViewModel = orderViewModel, groupViewModel = groupViewModel)
        }
        composable(route = AppScreens.Card_Screen.route) {
            CardInfoScreen(navController = navController, viewModel = cardViewModel)
        }
        composable(route = AppScreens.Payment.route) {
            PaymentScreen(navController = navController, viewModel = cardViewModel)
        }
    }
}