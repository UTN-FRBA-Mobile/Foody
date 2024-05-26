package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun CartScreen(navController: NavHostController, viewModel: MainViewModel) {
    var order = viewModel.getPickedOrder()
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
    val button_cart=ButtonInterface(
        resourceId = R.drawable.cart_icon,
        imageDescription = "Cart Icon",
        route = AppScreens.Cart_Screen.route,
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
    val viewModel = MainViewModel()
    CartScreen(navController, viewModel)
}
