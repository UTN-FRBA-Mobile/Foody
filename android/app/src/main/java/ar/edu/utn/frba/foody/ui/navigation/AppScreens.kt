package ar.edu.utn.frba.foody.ui.navigation

sealed class AppScreens(val route: String) {
    object Home_Screen: AppScreens("Home")
    object Login_Screen: AppScreens("Login")
    object SignUp_Screen: AppScreens("SignUp")
    object Location_Screen: AppScreens("Location")
    object Profile_Screen: AppScreens("Profile")
    object Cart_Screen: AppScreens("Cart")
    object Orders_Screen: AppScreens("Orders")
    object Restaurant_Screen: AppScreens("Restaurant")
    object Order_Screen: AppScreens("Order")
    object Progress_Order_Screen: AppScreens("Progress Order")
    object Group_Screen: AppScreens("Group")
    object Create_Group_Screen: AppScreens("Create Group")
    object Card_Screen: AppScreens("Card")
    object Payment: AppScreens("Payment")
    object Location2_Screen: AppScreens("Location2")
}