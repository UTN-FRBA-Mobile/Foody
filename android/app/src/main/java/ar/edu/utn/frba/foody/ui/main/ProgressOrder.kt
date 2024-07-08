package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun ProgressOrderScreen(
    navController: NavController, orderViewModel: OrderViewModel, order_id: String
) {
    val order = orderViewModel.getOrderById(order_id)

    AppScaffold(
        null,
        { TopGroupProgressOrder(navController = navController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_signup),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Column(modifier = Modifier.fillMaxWidth()) {
                if (order.orderStates.isNotEmpty() && order.orderStates[1].completed) {
                    TextInfo(text = "Repartidor Asignado: " + order.delivery?.email)
                }

                TextInfo(text = "Total Sin Envio: $" + order.userOrders.sumOf { userOrder ->
                    userOrder.items.sumOf { x -> x.quantity * x.dish.price }
                }.toString())

                TextInfo(text = "Total : $" + order.amount.toString())

                TextInfo(text = "Dirección de Entrega : " + order.address)

                if (order.card.equals("Efectivo")) {
                    TextInfo(text = "Pago Realizado en Efectivo")
                } else {
                    TextInfo(
                        text = "Tarjeta Usada : **** **** **** ${
                            order.card.takeLast(4)
                        }"
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Card(
                    modifier = Modifier
                        .padding(
                            start = 74.dp,
                            end = 74.dp
                        ),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 4.dp
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp, 0.dp)
                        ) {
                            order.orderStates.forEach {
                                StateProgressLine(
                                    state = it.completed, last = it.firstState
                                )

                                StateIcon(
                                    image = it.resourceId,
                                    imageDescription = it.imageDescription,
                                    state = it.completed
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        StateDescriptions(order.orderStates)
                    }

                }

            }
        }
    }
}

@Composable
fun StateDescriptions(states: List<OrderState>) {
    Column(modifier = Modifier.padding(16.dp, 0.dp)) {
        var textColor: Color
        states.forEach {
            textColor = if (it.current) {
                Color.Black
            } else if (it.completed) {
                Color.DarkGray
            } else {
                Color.LightGray
            }
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                if (it.completed) {
                    CompletedStateDescription(
                        textDescription = it.description, textColor = textColor
                    )
                } else {
                    PendingStateDescription(textDescription = it.description, textColor = textColor)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CompletedStateDescription(textDescription: String, textColor: Color) {
    Image(
        painter = painterResource(id = R.drawable.success_icon),
        contentDescription = "Success Icon",
        modifier = Modifier.size(18.dp),
        contentScale = ContentScale.FillBounds,
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text(
        text = textDescription, color = textColor
    )
}

@Composable
fun PendingStateDescription(textDescription: String, textColor: Color) {
    Spacer(modifier = Modifier.width(3.dp))
    Image(
        painter = painterResource(id = R.drawable.dot_icon),
        contentDescription = "Dot Icon",
        modifier = Modifier.size(10.dp),
        contentScale = ContentScale.FillBounds,
    )
    Spacer(modifier = Modifier.width(14.dp))
    Text(
        text = textDescription, color = textColor
    )
}

@Composable
fun TextInfo(text: String, align: Boolean = false) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp),
        fontSize = 24.sp,
        textAlign = if (align) TextAlign.Center else TextAlign.Start
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun StateIcon(image: Int, imageDescription: String, state: Boolean) {
    Surface(
        color = if (state) Color.Green else Color.LightGray,
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
fun StateProgressLine(state: Boolean, last: Boolean) {
    if (!last) {
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(2.dp)
                .background(if (state) Color.Green else Color.LightGray)
        )
    }
}

@Composable
fun TopGroupProgressOrder(navController: NavController) {
    TopAppBar(title = {
        Text(text = stringResource(id = R.string.app_name))
    }, actions = {
        IconButton(onClick = { navController.navigate(AppScreens.Orders_Screen.route) }) {
            Image(
                painter = painterResource(id = R.drawable.go_back),
                contentDescription = "Go Back Icon",
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    })
}

/*
@Preview
@Composable
fun ProgressOrderPreview() {
    val navController= rememberNavController()
    val viewModel = OrderViewModel()
    ProgressOrderScreen(navController, viewModel)
}*/
