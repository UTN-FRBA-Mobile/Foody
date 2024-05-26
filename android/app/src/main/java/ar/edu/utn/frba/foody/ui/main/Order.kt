package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun OrdersScreen(navController: NavController, viewModel: OrderViewModel) {
    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupOrder(navController = navController)}
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
                for (order in viewModel.orders) {
                    item {
                        OrderItem(navController = navController,
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
fun TopGroupOrder(navController: NavController) {
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
fun OrderItem(navController: NavController, viewModel: OrderViewModel, order: Order.OrderInfo) {
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
                        IconButton(onClick = { navController.navigate(AppScreens.Progress_Order_Screen.route) }) {
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

@Preview
@Composable
fun Default() {
    val navController= rememberNavController()
    val viewModel = OrderViewModel()
    OrdersScreen(navController, viewModel)
}