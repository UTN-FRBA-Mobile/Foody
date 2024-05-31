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
import ar.edu.utn.frba.foody.ui.Classes.OrderState
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun ProgressOrderScreen(navController: NavController, orderViewModel: OrderViewModel) {
    val order = orderViewModel.getPickedOrder()

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupProgressOrder(navController = navController)}
    ) {
        Box(modifier = Modifier.fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                TextInfo(text = "Direccion: " + order.direction)
                TextInfo(text = "Hora estimada: " + order.estimatedHour)
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(300.dp),
                    elevation = 4.dp
                ) {
                    Column(verticalArrangement = Arrangement.Center) {
                        TextInfo(text = "Estado", align = true)
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(16.dp, 0.dp)
                        ) {
                            order.orderStates.forEach{
                                StateProgressLine(state = it.completed,
                                    last = it.firstState
                                )
                                StateIcon(image = it.resourceId,
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
        states.forEach{
            textColor = if (it.current) {
                Color.Black
            } else if (it.completed) {
                Color.DarkGray
            } else {
                Color.LightGray
            }
            Row(verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (it.completed) {
                    CompletedStateDescription(textDescription = it.description, textColor = textColor)
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
    Text(text = textDescription,
        color = textColor
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
    Text(text = textDescription,
        color = textColor
    )
}

@Composable
fun TextInfo(text: String, align: Boolean = false) {
    Text(text = text,
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