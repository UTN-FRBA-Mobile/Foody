package ar.edu.utn.frba.foody.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.UserDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.main.CardInfoScreen
import ar.edu.utn.frba.foody.ui.main.CartScreen
import ar.edu.utn.frba.foody.ui.main.CreateGroupScreen
import ar.edu.utn.frba.foody.ui.main.GroupScreen
import ar.edu.utn.frba.foody.ui.main.HomeScreen
import ar.edu.utn.frba.foody.ui.main.JoinGroupScreen
import ar.edu.utn.frba.foody.ui.main.LocationGoogleScreen
import ar.edu.utn.frba.foody.ui.main.LoginScreen
import ar.edu.utn.frba.foody.ui.main.OrderScreen
import ar.edu.utn.frba.foody.ui.main.OrdersDeliveredScreen
import ar.edu.utn.frba.foody.ui.main.OrdersOnTheWayScreen
import ar.edu.utn.frba.foody.ui.main.OrdersScreen
import ar.edu.utn.frba.foody.ui.main.PaymentScreen
import ar.edu.utn.frba.foody.ui.main.PendingOrderScreen
import ar.edu.utn.frba.foody.ui.main.ProfileScreen
import ar.edu.utn.frba.foody.ui.main.ProgressOrderScreen
import ar.edu.utn.frba.foody.ui.main.RestaurantScreen
import ar.edu.utn.frba.foody.ui.main.SessionScreen
import ar.edu.utn.frba.foody.ui.main.SignUpScreen
import ar.edu.utn.frba.foody.ui.main.SplashScreen

@Composable
fun AppNavigation(
    context: ComponentActivity,
    navController: NavHostController,
    viewModel: MainViewModel,
    orderViewModel: OrderViewModel,
    groupViewModel: GroupViewModel,
    dbRestaurantHelper: RestaurantDataBase,
    dbUserDataBaseFirebase: UserDataBaseFirebase
) {
    NavHost(navController = navController, startDestination = AppScreens.Splash_Screen.route) {
        composable(route = AppScreens.Session_Screen.route)
        {
            SessionScreen(
                navController = navController,
                viewModel = viewModel,
                restaurantDataBase = dbRestaurantHelper,
                orderViewModel = orderViewModel,
                groupViewModel = groupViewModel
            )
        }
        composable(route = AppScreens.Splash_Screen.route){
            SplashScreen(navController = navController)
        }
        composable(route = AppScreens.Home_Screen.route) {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                restaurantDataBase = dbRestaurantHelper,
                orderViewModel = orderViewModel,
                groupViewModel = groupViewModel
            )
        }
        composable(route = AppScreens.Login_Screen.route) {
            LoginScreen(
                navController = navController,
                mainViewModel = viewModel,
                orderViewModel = orderViewModel
            )
        }
        composable(route = AppScreens.SignUp_Screen.route) {
            SignUpScreen(
                navController = navController,
                viewModel = orderViewModel,
                mainViewModel = viewModel,
                dbUserDataBaseFirebase
            )
        }
        composable(route = AppScreens.Profile_Screen.route) {
            ProfileScreen(
                navController = navController, viewModel = viewModel, orderViewModel
            )
        }
        composable(route = AppScreens.Cart_Screen.route) { backStackEntry ->
            CartScreen(
                navController = navController,
                viewModel = orderViewModel,
                groupViewModel = groupViewModel,
                origin = backStackEntry.arguments?.getString("origin") ?: "unknown",
            )
        }
        composable(route = AppScreens.Orders_Screen.route) {
            OrdersScreen(navController = navController, orderViewModel)
        }
        composable(route = AppScreens.Restaurant_Screen.route) {
            RestaurantScreen(
                navController = navController,
                viewModel = viewModel,
                orderViewModel = orderViewModel
            )
        }
        composable(
            route = AppScreens.Order_Screen.route,
            arguments = listOf(navArgument("order_id") { type = NavType.StringType },
                navArgument("origin") { type = NavType.StringType }),
        ) {
                backStackEntry ->
            OrderScreen(navController = navController, viewModel = orderViewModel,
                backStackEntry.arguments?.getString("order_id") ?: "unknown",
                backStackEntry.arguments?.getString("origin") ?: "unknown")
        }
        composable(
            route = AppScreens.Progress_Order_Screen.route,
            arguments = listOf(navArgument("order_id") { type = NavType.StringType }),

        ) {backStackEntry->
            ProgressOrderScreen(navController = navController, orderViewModel = orderViewModel,
                backStackEntry.arguments?.getString("order_id") ?: "unknown"
            )
        }
        composable(route = AppScreens.Group_Screen.route) {
            GroupScreen(
                navController = navController,
                orderViewModel = orderViewModel,
                groupViewModel = groupViewModel
            )
        }
        composable(route = AppScreens.Create_Group_Screen.route) {
            CreateGroupScreen(
                navController = navController,
                mainViewModel = viewModel,
                orderViewModel = orderViewModel,
                groupViewModel = groupViewModel,
            )
        }
        composable(route = AppScreens.Join_Group_Screen.route) {
            JoinGroupScreen(
                navController = navController,
                mainViewModel = viewModel,
                orderViewModel = orderViewModel,
                groupViewModel = groupViewModel
            )
        }
        composable(route = AppScreens.Card_Screen.route) {
            CardInfoScreen(navController = navController,viewModel,orderViewModel)
        }
        composable(route = AppScreens.Payment.route) {
            PaymentScreen(navController = navController,viewModel,orderViewModel)
        }
        composable(
            route = AppScreens.Location_Screen.route,
            arguments = listOf(navArgument("origin") { type = NavType.StringType },
                navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            LocationGoogleScreen(
                context, navController, orderViewModel,
                origin = backStackEntry.arguments?.getString("origin") ?: "unknown",
                id = backStackEntry.arguments?.getString("id") ?: "unknown"
            )
        }
        composable(route = AppScreens.PendingOrder.route) {
            PendingOrderScreen(navController = navController,viewModel,orderViewModel)
        }
        composable(route = AppScreens.OrdersDeliverd.route) {
            OrdersDeliveredScreen(navController = navController,orderViewModel)
        }
        composable(route = AppScreens.OnTheWayOrders.route) {
            OrdersOnTheWayScreen(navController = navController,orderViewModel)
        }
    }
}