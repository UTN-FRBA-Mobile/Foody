package ar.edu.utn.frba.foody.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.UserDataBase
import ar.edu.utn.frba.foody.ui.dataBase.StoreUserSession.StoreUserSession
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import kotlinx.coroutines.delay

@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: MainViewModel,
    restaurantDataBase: RestaurantDataBase?,
    userDataBase: UserDataBase?,
    orderViewModel: OrderViewModel
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserSession(context)
    val userSession = dataStore.getSession.collectAsState(initial = "")

    AppScaffold(
        navController,
        null,
    ) {
        if (userSession.value == "") {
            LoginScreen(
                navController = navController as NavHostController,
                mainViewModel = viewModel
            )
        } else {
            HomeScreen(
                navController = navController,
                viewModel = viewModel,
                restaurantDataBase = restaurantDataBase,
                userDataBase = userDataBase,
                orderViewModel = orderViewModel
            )
        }
    }
}