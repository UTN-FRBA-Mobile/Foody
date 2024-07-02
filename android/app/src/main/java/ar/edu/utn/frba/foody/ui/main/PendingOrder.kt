package ar.edu.utn.frba.foody.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.composables.SimpleAlert
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.UserDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun PendingOrderScreen(
    navController: NavController,
    viewModel: MainViewModel,
    orderViewModel: OrderViewModel
) {
    AppScaffold(
        navController,
        null,
        { BottomGroupPendingOrder(navController, orderViewModel) },
        { TopGroupPendingOrder(navController, viewModel) }
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
                    text = "Orders",
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
                for (order in orderViewModel.getAllOrdersByState()) {
                        item {
                            OrderPending(
                                navController = navController,
                                viewModel = viewModel,
                                orderViewModel = orderViewModel,
                                order = order
                            )
                        }
                    }
                    item {
                        if (orderViewModel.getAllOrdersByState().isNotEmpty())
                            Divider()
                    }

            }
        }
    }

}

@Composable
fun OrderPending(
    navController: NavController,
    viewModel: MainViewModel,
    orderViewModel: OrderViewModel,
    order: Order
) {
    val showAlert = remember {
        mutableStateOf(false)
    }
    val showAccept = remember {
        mutableStateOf(false)
    }

    val restaurantName = viewModel.getPickedRestaurantName()

    SimpleAlert(
        show = showAlert.value,
        text = "Estas seguro que deseas aceptar este pedido?",
        onConfirm = {
            showAlert.value = false; showAccept.value = true
        },
        onDismiss = { showAlert.value = false }
    )
    if (showAccept.value) {
        //cambiar estado a en camino y hacer que aparezca solo esa orden y puedas pasarla a finalizado
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Restaurante: ${order.restaurant.name}", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text(text = "Cliente: ${order.userOrders.get(0).user.userId}", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "Monto Pagado: ${order.montoPagado}", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
                Text(text = "Dirección: ${order.direction}", fontSize = 16.sp, modifier = Modifier.padding(vertical = 4.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButton(onClick = { showAlert.value=true }) {
                    Image(
                        painter = painterResource(id = R.drawable.check), // Asegúrate de tener un recurso de ícono para el tilde
                        contentDescription = "Aceptar",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                IconButton(onClick = { /* Acción para la cruz */ }) {
                    Image(
                        painter = painterResource(id = R.drawable.wrong), // Asegúrate de tener un recurso de ícono para la cruz
                        contentDescription = "Rechazar",
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}
@Composable
fun BottomGroupPendingOrder(navController: NavController, orderViewModel: OrderViewModel) {
    val buttons = listOf(
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
        ButtonInterface(
            resourceId = R.drawable.create_group_icon,
            imageDescription = "Join Group Icon",
            route = AppScreens.Join_Group_Screen.route
        )
    )

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
fun TopGroupPendingOrder(navController: NavController, viewModel: MainViewModel) {
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.app_name),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = { navController.navigate(AppScreens.Home_Screen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.go_back),
                    contentDescription = "Go Back Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        },
        actions = {
            IconButton(onClick = { /* Action for repartidor */ }) {
                Image(
                    painter = painterResource(id = R.drawable.repartidor),
                    contentDescription = "Repartidor Icon",
                    modifier = Modifier.size(30.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
            IconButton(onClick = { viewModel.logout() }) {
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
