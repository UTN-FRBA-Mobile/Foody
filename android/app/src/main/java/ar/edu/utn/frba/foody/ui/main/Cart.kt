package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.composables.DishAlert
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun CartScreen(
    navController: NavHostController,
    viewModel: OrderViewModel,
    origin: String,
) {
    val order = viewModel.getPickedOrder()
    AppScaffold(navController,
        null,
        { BottomGroupCart(navController, orderViewModel = viewModel, order = order) },
        { TopGroupCart(navController, origin) }) {
        OrdersGrid(viewModel, order.userOrders,navController)
    }
}

@Composable
fun TopGroupCart(navController: NavController, origin: String) {
    val route =
        if (origin == "home") AppScreens.Home_Screen.route else AppScreens.Restaurant_Screen.route

    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(route) }) {
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
fun BottomGroupCart(
    navController: NavController,
    orderViewModel: OrderViewModel,
    order: Order
) {
    val buttons = mutableListOf(
        ButtonInterface(
            resourceId = R.drawable.payment_icon,
            imageDescription = "Payment Icon",
            route = AppScreens.Payment.route,
        )
    )

    if (order.group != null) {
        buttons.add(
            ButtonInterface(
                resourceId = R.drawable.group_icon,
                imageDescription = "Group Icon",
                route = AppScreens.Group_Screen.route
            )
        )
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(8.dp)
    ) {
        Text(
            text = "Total: $" + orderViewModel.getTotal(),
            modifier = Modifier
                .weight(2f)
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            style = MaterialTheme.typography.h6
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = { orderViewModel.emptyUserOrder() },
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.empty_cart_icon),
                contentDescription = "Empty Cart Icon",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.FillBounds
            )
        }


        // Buttons
        buttons.forEach { button ->
            IconButton(
                onClick = { navController.navigate(button.route) },
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                Image(
                    painter = painterResource(id = button.resourceId),
                    contentDescription = button.imageDescription,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun OrdersGrid(
    viewModel: OrderViewModel,
    userOrders: List<UserOrder>,
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
                OrderCard(viewModel, userOrders[index], navController)
            }
        }
    }
    Spacer(modifier = Modifier.width(8.dp))
    if(viewModel.getPickedOrder().userOrders.isNotEmpty() ||
        viewModel.getPickedOrder().userOrders.any { userOrder -> userOrder.items.isNotEmpty()}){
        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    navController.navigate(AppScreens.Payment.route)
                },
                enabled = viewModel.enablePayOrder(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Pagar", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun OrderCard(viewModel: OrderViewModel, userOrder: UserOrder,navController: NavController) {
    val heightContent = if (userOrder.items.size > 1) 170.dp else 90.dp

    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp, 8.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = userOrder.user.email,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.height(heightContent),
            ) {
                items(userOrder.items.size) { index ->
                    OrderItem(viewModel, userOrder.items[index], userOrder)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Total: $" + userOrder.items.sumOf { x -> x.quantity * x.dish.price }
                    .toString(),
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

@Composable
fun OrderItem(viewModel: OrderViewModel, orderItem: OrderItemInfo, userOrder: UserOrder) {
    val showDialog = remember { mutableStateOf(false) }
    val totalPrice = orderItem.quantity * orderItem.dish.price

    DishAlert(show = showDialog.value, dish = orderItem.dish, totalPrice) {
        showDialog.value = false
    }

    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(60.dp),
        elevation = 4.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .clickable { showDialog.value = true },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                viewModel.deleteItem(orderItem.dish.dishId)
                },
                enabled = viewModel.enableChangeUserOrderButton(userOrder.user.userId)
            ) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = orderItem.dish.name, modifier = Modifier.width(100.dp))

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = {
                viewModel.changeItemQuantity(orderItem.dish.dishId, 1)
                },
                enabled = viewModel.enableChangeUserOrderButton(userOrder.user.userId)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
            }

            Text(text = orderItem.quantity.toString(), fontSize = 18.sp)

            IconButton(onClick = {
                viewModel.changeItemQuantity(orderItem.dish.dishId, -1)
                },
                enabled = viewModel.enableChangeUserOrderButton(userOrder.user.userId)
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
            }
        }
    }
}

/*
@Preview
@Composable
fun DefaultPreviewCart() {
    val navController = rememberNavController()
    val viewModel = OrderViewModel()
    CartScreen(navController, viewModel, origin = "home")
}*/
