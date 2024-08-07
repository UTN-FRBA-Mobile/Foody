package ar.edu.utn.frba.foody.ui.main

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.composables.SimpleAlert
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.StoreUserSession.StoreUserSession
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: MainViewModel,
    restaurantDataBase: RestaurantDataBase?,
    orderViewModel: OrderViewModel,
    groupViewModel: GroupViewModel
) {
    val canGoBack = remember { mutableStateOf(false) }

    BackHandler(enabled = !canGoBack.value) {
    }
    orderViewModel.findAllOrdersByState()
    groupViewModel.findGroupByUserId()

    AppScaffold(
        { BottomGroupHome(navController,orderViewModel) },
        { TopGroupHome(navController, viewModel) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_signup),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 32.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Restaurantes",
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(505.dp)
            ) {
                if (restaurantDataBase != null) {
                    for (restaurant in restaurantDataBase.getAllRestaurants(restaurantDataBase)) {
                        item {
                            RestaurantItem(
                                navController = navController,
                                viewModel = viewModel,
                                orderViewModel = orderViewModel,
                                restaurant = restaurant
                            )
                        }
                    }
                    item {
                        if (restaurantDataBase.getAllRestaurants(restaurantDataBase).isNotEmpty())
                            Divider()
                    }
                }
            }
        }
    }

}

@Composable
fun RestaurantItem(
    navController: NavController,
    viewModel: MainViewModel,
    orderViewModel: OrderViewModel,
    restaurant: Restaurant
) {
    val showAlert = remember {
        mutableStateOf(false)
    }
    val showRestaurant = remember {
        mutableStateOf(false)
    }
    val changeRestaurant = remember {
        mutableStateOf(false)
    }
    var showError by remember { mutableStateOf(false) }

    val restaurantName = orderViewModel.getPickedOrder().restaurant.name
    val group = orderViewModel.getPickedOrder().group

    SimpleAlert(
        show = showAlert.value,
        text = "Actualmente tienes un pedido con el restaurante ${restaurantName}, si eliges este restaurante perderás el pedido actual. ¿Deseas continuar?",
        onConfirm = {
            showAlert.value = false; showRestaurant.value = true; changeRestaurant.value = true
        },
        onDismiss = { showAlert.value = false }
    )
    if (showRestaurant.value) {
        viewModel.updateRestaurant(restaurant)
        navController.navigate(AppScreens.Restaurant_Screen.route)
    }
    if (changeRestaurant.value) {
        orderViewModel.changeRestaurant(restaurant)
    }

    Card(
        modifier = Modifier
            .fillMaxSize(),
        backgroundColor = Color.Transparent,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    //enabled = group == null || restaurantName == restaurant.name,
                    onClick = {
                        if (group != null) {
                            if (restaurantName != restaurant.name) {
                                showError = true
                                Toast.makeText(
                                    navController.context,
                                    "No podes ordenar en este restaurante",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showRestaurant.value = true
                            }
                        } else {
                            if (restaurantName != "" &&
                                restaurant.name != restaurantName) {
                                showAlert.value = true
                            } else {
                                showRestaurant.value = true
                            }
                        }
                    })
                .padding(16.dp, 4.dp)
        ) {
            Image(
                painter = painterResource(id = restaurant.image),
                contentDescription = restaurant.imageDescription,
                modifier = Modifier
                    .size(128.dp, 64.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = restaurant.name,
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
        Divider()
    }
}

data class ButtonInterface(
    val resourceId: Int,
    val imageDescription: String,
    val route: String
)


@Composable
fun BottomGroupHome(navController: NavController,orderViewModel: OrderViewModel) {
    val buttons = mutableListOf(
        ButtonInterface(
            resourceId = R.drawable.user_icon,
            imageDescription = "User Icon",
            route = AppScreens.Profile_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.cart_icon,
            imageDescription = "Cart Icon",
            route = AppScreens.Cart_Screen.createRoute(origin = "home")
        ),
        ButtonInterface(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            route = AppScreens.Orders_Screen.route
        ),
    )

    if (orderViewModel.user.groupId == "") {
        buttons.add(
            ButtonInterface(
                resourceId = R.drawable.create_group_icon,
                imageDescription = "Join Group Icon",
                route = AppScreens.Join_Group_Screen.route
            )
        )
    }

    BottomAppBar {
        Row(modifier = Modifier.fillMaxWidth()) {
            buttons.forEach {
                IconButton(
                    onClick = { navController.navigate(it.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Image(
                        painter = painterResource(id = it.resourceId),
                        contentDescription = it.imageDescription,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}

@Composable
fun TopGroupHome(navController: NavController, viewModel: MainViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserSession(context)

    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },

        actions = {
            if (viewModel.user.value?.delivery.equals("Si")) {
                IconButton(onClick = { navController.navigate(AppScreens.PendingOrder.route) }) {
                    Image(
                        painter = painterResource(id = R.drawable.repartidor),
                        contentDescription = "Repartidor Icon",
                        modifier = Modifier.size(30.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
            IconButton(onClick = {
                viewModel.logout()
                scope.launch {
                    dataStore.deleteSession()
                }
            }) {
                Image(
                    painter = painterResource(id = R.drawable.logout_icon),
                    contentDescription = "Logout Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    )
}