package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.*
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.*
import ar.edu.utn.frba.foody.ui.composables.DishAlert
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun OrderScreen(
    navController: NavHostController,
    viewModel: OrderViewModel,
    order_id: String,
    origin: String
) {
    val order = viewModel.getOrderById(order_id)

    AppScaffold(navController,
        null,
       null,
        { TopGroupOrder(navController,origin) }) {
        OrderDetailGrid(order.userOrders,order,origin,viewModel,navController)
    }
}

@Composable
fun TopGroupOrder(navController: NavController,origin: String) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { when (origin) {
                "ordersList" -> navController.navigate(AppScreens.Orders_Screen.route)
                "ordersDelivered" -> navController.navigate(AppScreens.OrdersDeliverd.route)
                "ordersOnTheWay" -> navController.navigate(AppScreens.OnTheWayOrders.route)
            }}) {
                Image(
                    painter = painterResource(id = R.drawable.go_back),
                    contentDescription = "Go Back Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    )
}

@Composable
fun BottomGroupOrder(
    orderViewModel: OrderViewModel,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(8.dp)
    ) {
        Text(
            text = "Total: $" + orderViewModel.getTotal(),
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OrderDetailGrid(
    userOrders: List<UserOrder>,
    order: Order,
    origin: String,
    orderViewModel: OrderViewModel,
    navController: NavController
) {
    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize(),

        ) {
        items(userOrders.size) { index ->
            if (userOrders[index].items.isNotEmpty()) {
                OrderDetailCard(userOrders[index], order, origin,
                    orderViewModel,navController )
            }
        }
    }
}

@Composable
fun OrderDetailCard(userOrder: UserOrder,order:Order,origin: String,
                    orderViewModel: OrderViewModel,navController: NavController) {
    val heightContent = if (userOrder.items.size > 1) 170.dp else 90.dp

    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp, 8.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = order.restaurant.name ,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.height(heightContent),
            ) {
                items(userOrder.items.size) { index ->
                    OrderDetailItem(userOrder.items[index])
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total Sin Envio: $" + userOrder.items.sumOf { x -> x.quantity * x.dish.price }
                    .toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total : $" + order.montoPagado.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Dirección de entrega : " + order.direction,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (order.tarjetaUsada.equals("Efectivo")){
                Text(
                    text = "Pago Realizado en Efectivo",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            else {
                Text(
                    text = "Tarjeta usada : **** **** **** ${
                        order.tarjetaUsada.takeLast(4)
                    }",
                    style = MaterialTheme.typography.body1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            if(origin=="ordersOnTheWay" || origin=="ordersList"){
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Usuario Comprador : " + order.userOrders.get(0).user.userId,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            else{
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Repartidor Asignado: " + order.repartidor?.userId,
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            if(origin=="ordersOnTheWay") {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        order.estado=Estado.FINALIZADO
                        orderViewModel.updateDataBaseOrder(order)
                        navController.navigate(AppScreens.OnTheWayOrders.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Entregado", fontSize = 18.sp)
                }
            }

        }
    }
}

@Composable
fun OrderDetailItem(orderItem: OrderItemInfo) {
    val showDialog = remember { mutableStateOf(false) }
    val totalPrice = orderItem.quantity * orderItem.dish.price

    DishAlert(show = showDialog.value, dish = orderItem.dish, totalPrice) {
        showDialog.value = false
    }

    Card(
        modifier = Modifier
            .padding(10.dp)
            .fillMaxWidth()
            .height(60.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { showDialog.value = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = orderItem.dish.name)

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = orderItem.quantity.toString(), fontSize = 18.sp)
        }
    }
}


/*
@Preview
@Composable
fun DefaultPreviewOrder() {
    val navController = rememberNavController()
    val viewModel = OrderViewModel()
    OrderScreen(navController, viewModel)
}*/
