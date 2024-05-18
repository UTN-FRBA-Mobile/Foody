package ar.edu.utn.frba.foody.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.main.HomeScreen
import ar.edu.utn.frba.foody.ui.main.LoginScreen
import ar.edu.utn.frba.foody.ui.main.MainViewModel

@Composable
fun AppNavigation(){
    val navController= rememberNavController()
    NavHost(navController = navController , startDestination = AppScreens.Login_Screen.route) {
        composable(route = AppScreens.Home_Screen.route){
            val viewModel = viewModel<MainViewModel>()
            viewModel.labelBotonIngresar = "boton"
            HomeScreen(viewModel = viewModel, onButtonClicked ={},navController )
        }
        composable(route = AppScreens.Login_Screen.route){
            LoginScreen(navController = navController)
        }
    }
}