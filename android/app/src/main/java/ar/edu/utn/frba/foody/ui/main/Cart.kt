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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun CartScreen(navController: NavHostController, viewModel: OrderViewModel, groupViewModel: GroupViewModel) {
    val order = viewModel.getPickedOrder()
    groupViewModel.updateGroup(order.group!!)
    AppScaffold(navController, stringResource(id = R.string.label_titulo_carrito), {BottomGroupCart(navController, orderViewModel = viewModel, order = order)},
        { TopGroupCart(navController) }){
        OrdersGrid(navController = navController, viewModel, order.userOrders)
    }
}

@Composable
fun TopGroupCart(navController: NavController) {
    val button_go_back =
        ButtonInterface(
            resourceId = R.drawable.go_back,
            imageDescription = "Go Back Icon",
            route = AppScreens.Home_Screen.route
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
fun BottomGroupCart(navController: NavController,
                    orderViewModel: OrderViewModel,
                    order: Order
) {
    val buttons = listOf(
        ButtonInterface(
            resourceId = R.drawable.empty_cart_icon,
            imageDescription = "Empty Cart Icon",
            route = AppScreens.Profile_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.payment_icon,
            imageDescription = "Payment Icon",
            route = AppScreens.Profile_Screen.route,
        ),
        ButtonInterface(
            resourceId = if (order.group != null) R.drawable.group_icon else R.drawable.create_group_icon,
            imageDescription = "Group Icon",
            route = if (order.group != null) AppScreens.Group_Screen.route else AppScreens.Create_Group_Screen.route
        )
    )

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
fun OrdersGrid(navController: NavController, viewModel: OrderViewModel, userOrders: List<UserOrder>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(1),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize(),

    ) {
        items(userOrders.size) { index ->
            OrderCard(navController, viewModel, userOrders[index])
        }
    }
}

@Composable
fun OrderCard(navController: NavController, viewModel: OrderViewModel, userOrder: UserOrder) {
    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth(),
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
                modifier = Modifier.height(140.dp),
            ) {
                items(userOrder.items.size) { index ->
                    OrderItem(viewModel, userOrder.items[index], userOrder)
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
fun OrderItem(viewModel: OrderViewModel, orderItem: OrderItemInfo, userOrder: UserOrder) {
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
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { viewModel.deleteItem(userOrderId = userOrder.userOrderId, userItemId = orderItem.id) }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = "Remove")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(text = orderItem.dish.name)

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(onClick = { viewModel.changeItemQuantity(userOrder.userOrderId, orderItem.id, 1) }) {
                Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
            }

            Text(text = orderItem.quantity.toString(), fontSize = 18.sp)

            IconButton(onClick = { viewModel.changeItemQuantity(userOrder.userOrderId, orderItem.id, -1) }) {
                Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
            }
        }
    }
}

/*
@Preview
@Composable
fun DefaultPreviewOrder() {
    val navController= rememberNavController()
    val viewModel = OrderViewModel()
    val groupViewModel = GroupViewModel()
    CartScreen(navController, viewModel, groupViewModel)
}
 */
