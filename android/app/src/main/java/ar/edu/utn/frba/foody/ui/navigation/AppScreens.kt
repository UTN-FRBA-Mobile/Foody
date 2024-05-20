package ar.edu.utn.frba.foody.ui.navigation

sealed class AppScreens(val route: String) {
    object Home_Screen: AppScreens("HomeScreen")
    object Login_Screen: AppScreens("Login")
    object SignUp_Screen: AppScreens("SignUp")
}