package ar.edu.utn.frba.foody.ui.navigation

sealed class AppScreens(val route: String) {
    object Home_Screen: AppScreens("HomeScreen")
    object Login_Screen: AppScreens("Login")
    object Profile_Screen: AppScreens("Profile")
    object Cart_Screen: AppScreens("Cart")
    object Order_Screen: AppScreens("Order")
    object Restaurant_Screen: AppScreens("Restaurant")
}