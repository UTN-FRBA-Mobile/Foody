package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun OrdersScreen(navController: NavController, viewModel: OrderViewModel) {
    val order = viewModel.getPickedOrder()

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupOrderList(navController = navController)}
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
                .fillMaxWidth()
            ) {
                Text(text = "Orders",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(505.dp)
            ) {
                    item {
                        if (viewModel.hasItems(order)) {
                            OrderItem(
                                navController = navController,
                                viewModel = viewModel,
                                order = order
                            )
                        }
                    }
            }
        }
    }
}

@Composable
fun TopGroupOrderList(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Home_Screen.route) }) {
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
fun OrderItem(navController: NavController, viewModel: OrderViewModel, order: Order) {
    Card(backgroundColor = MaterialTheme.colors.secondary) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.align(Alignment.CenterVertically)
                .weight(0.7f),
                contentAlignment = Alignment.Center
            ) {
                Text(text = order.name,
                    textAlign = TextAlign.Center
                )
            }
            Box(modifier = Modifier.weight(0.3f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { viewModel.updateOrder(order)
                        navController.navigate(AppScreens.Order_Screen.route) }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.details_icon),
                            contentDescription = "View Details Icon",
                            modifier = Modifier.size(32.dp, 20.dp),
                            contentScale = ContentScale.FillBounds
                        )
                    }
                    if (order.inProgress) {
                        IconButton(onClick = { viewModel.updateOrder(order)
                            navController.navigate(AppScreens.Progress_Order_Screen.route) }) {
                            Image(
                                painter = painterResource(id = R.drawable.order_progress_icon),
                                contentDescription = "Order Progress Icon",
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                }
            }
        }
        Divider()
    }
}

/*
@Preview
@Composable
fun Default() {
    val navController= rememberNavController()
    val viewModel = OrderViewModel()
    OrdersScreen(navController, viewModel)
}
 */