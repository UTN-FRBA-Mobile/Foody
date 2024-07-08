package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import ar.edu.utn.frba.foody.ui.Classes.Status
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.composables.SimpleAlert
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun PendingOrderScreen(
    navController: NavController,
    viewModel: MainViewModel,
    orderViewModel: OrderViewModel
) {
    var refreshState by remember { mutableStateOf(false) }

    AppScaffold(
        { BottomGroupPendingOrder(navController) },
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
                            orderViewModel = orderViewModel,
                            order = order,
                            onRechazarClick = { refreshState = !refreshState }
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
    orderViewModel: OrderViewModel,
    order: Order,
    onRechazarClick: () -> Unit
) {
    val showAlert = remember {
        mutableStateOf(false)
    }
    val showAccept = remember {
        mutableStateOf(false)
    }

    SimpleAlert(
        show = showAlert.value,
        text = "Estas seguro que deseas aceptar este pedido?",
        onConfirm = {
            showAlert.value = false; showAccept.value = true
        },
        onDismiss = { showAlert.value = false }
    )
    if (showAccept.value) {
        order.status = Status.ONTHEWAY
        order.delivery = orderViewModel.user
        order.orderStates.get(0).current = false
        order.orderStates.get(1).completed = true
        order.orderStates.get(1).current = true
        orderViewModel.updateDataBaseOrder(order)
        var orders = orderViewModel.getPendingsOrders()
        orders = orders.filter { ordernew -> ordernew.orderId != order.orderId }
        orderViewModel.updatePendingOrders(orders)
        orderViewModel.findOrdersDeliveredById()

        navController.navigate(AppScreens.OnTheWayOrders.route)
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
                Text(
                    text = "Restaurante: ${order.restaurant.name}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Cliente: ${order.userOrders.get(0).user.userId}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Monto Pagado: ${order.amount}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    text = "Dirección: ${order.address}",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            if (showAccept.value == false) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { showAlert.value = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.check),
                            contentDescription = "Aceptar",
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    IconButton(onClick = {
                        var orders = orderViewModel.getPendingsOrders()
                        orders = orders.filter { ordernew -> ordernew.orderId != order.orderId }
                        orderViewModel.updatePendingOrders(orders)
                        onRechazarClick()
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.wrong),
                            contentDescription = "Rechazar",
                            modifier = Modifier.size(24.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomGroupPendingOrder(navController: NavController) {
    val buttons = listOf(
        ButtonInterface(
            resourceId = R.drawable.order_pending,
            imageDescription = "Order Pending",
            route = AppScreens.OnTheWayOrders.route
        ),
        ButtonInterface(
            resourceId = R.drawable.order_accept,
            imageDescription = "Order Delivered",
            route = AppScreens.OrdersDeliverd.route
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
                        modifier = Modifier.size(45.dp),
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
