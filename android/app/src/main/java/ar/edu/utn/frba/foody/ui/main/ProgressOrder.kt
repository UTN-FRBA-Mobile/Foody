package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

data class StateInterface(
    val resourceId: Int,
    val imageDescription: String,
    val description: String,
    val completed: Boolean,
    val last: Boolean = false,
)

@Composable
fun ProgressOrderScreen(navController: NavController, orderViewModel: OrderViewModel) {
    val order = orderViewModel.getPickedOrder()
    val states = listOf(
        StateInterface(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            description = "Recibimos tu pedido",
            completed = true,
            last = true
        ),
        StateInterface(
            resourceId = R.drawable.store_icon,
            imageDescription = "Store Icon",
            description = "Estamos preparando tu pedido",
            completed = true
        ),
        StateInterface(
            resourceId = R.drawable.delivery_icon,
            imageDescription = "Delivery Icon",
            description = "Tu pedido está en camino",
            completed = true
        ),
        StateInterface(
            resourceId = R.drawable.finished_icon,
            imageDescription = "Finished Icon",
            description = "Entregamos tu pedido",
            completed = false
        ),
    )

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupProgressOrder(navController = navController)}
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Direccion",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = (order.estimatedHour).toString(),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(250.dp),
                    elevation = 4.dp
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(text = "Estado",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontSize = 24.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp, 0.dp)) {
                            states.forEach{
                                PedidoLineaActiva(state = it.completed, last = it.last)
                                PedidoEstado(image = it.resourceId, imageDescription = it.imageDescription, state = it.completed)
                            }
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        Column(modifier = Modifier.padding(16.dp, 0.dp)) {
                            states.forEach{
                                Text(text = it.description, color = if (it.completed) Color.Black else Color.LightGray)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }

                }
            }
        }

    }
}

@Composable
fun PedidoEstado(image: Int, imageDescription: String, state: Boolean) {
    Surface(
        color = if (state) Color(0xFF00BB2D) else Color.LightGray,
        shape = CircleShape,
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = imageDescription,
                modifier = Modifier.size(18.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun PedidoLineaActiva(state: Boolean, last: Boolean) {
    if (!last) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(2.dp)
                .background(if (state) Color(0xFF00BB2D) else Color.LightGray)
        )
    }
}

@Composable
fun TopGroupProgressOrder(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Orders_Screen.route) }) {
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

@Preview
@Composable
fun ProgressOrderPreview() {
    val navController= rememberNavController()
    val viewModel = OrderViewModel()
    ProgressOrderScreen(navController, viewModel)
}