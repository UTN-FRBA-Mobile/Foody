package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.navigation.*
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.*
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun CartScreen(navController: NavHostController, viewModel: OrderViewModel) {
    val order = viewModel.getPickedOrder()
    AppScaffold(navController, stringResource(id = R.string.label_titulo_carrito), {BottomGroupCart(navController)},
        { TopGroupCart(navController) }){
        OrdersGrid(navController = navController, order.userOrders)
    }
}

@Composable
fun TopGroupCart(navController: NavController) {
    val button_go_back =
        ButtonInterface(
            resourceId = R.drawable.go_back,
            imageDescription = "Go Back Icon",
            route = AppScreens.Home_Screen.route,

            )
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primarySurface),
        horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(
            onClick = { navController.navigate(button_go_back.route) },
        ) {
            Image(
                painter = painterResource(id = button_go_back.resourceId),
                contentDescription = button_go_back.imageDescription,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.FillBounds
            )
        }
        Text(stringResource(id = R.string.label_titulo_carrito),
            Modifier
            .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center)
    }
}

@Composable
fun BottomGroupCart(navController: NavController) {
    val buttons = listOf(
        ButtonInterface(
            resourceId = R.drawable.user_icon,
            imageDescription = "User Icon",
            route = AppScreens.Profile_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            route = AppScreens.Order_Screen.route,
        )
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        buttons.forEach{
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

@Composable
fun OrdersGrid(navController: NavController, userOrders: List<UserOrder>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(userOrders.size) { index ->
            OrderCard(navController, userOrders[index])
        }
    }
}

@Composable
fun OrderCard(navController: NavController, userOrder: UserOrder) {
    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(200.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { /* Navigate to restaurant details */ }
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = userOrder.user.userName,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(userOrder.items.size) { index ->
                    OrderItem(navController, userOrder.items[index], userOrder)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $" + userOrder.items.sumOf { x -> x.quantity * x.dish.price }.toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun OrderItem(navController: NavController, orderItem: OrderItemInfo, userOrder: UserOrder) {
    var quantity by remember { mutableStateOf(orderItem.quantity) }

    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(70.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Borrar item */ }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = orderItem.dish.name)

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { quantity++ }) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
            }

            Text(text = quantity.toString(), fontSize = 18.sp)

            IconButton(onClick = { if (quantity > 0) quantity-- }) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreviewOrder() {
    val navController= rememberNavController()
    val viewModel = OrderViewModel()
    CartScreen(navController, viewModel)
}
