package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun PaymentScreen(navController: NavHostController,
                  mainViewModel: MainViewModel,orderViewModel: OrderViewModel) {
    var totalAmount = orderViewModel.getTotal()
    var deliveryFee = 500.0
    val user = orderViewModel.user
    var address by remember { mutableStateOf(user.direccion) }
    var paymentMethod by remember { mutableStateOf("Efectivo") }
    val paymentOptions = listOf("Efectivo", "Tarjeta")
    val totalPayment = totalAmount + deliveryFee
    var cards = orderViewModel.user.tarjetas

    AppScaffold(navController,
        null,
        null,
        { TopGroupPayment(navController) }
    ) {
        // Simulated list of preloaded cards
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.background_signup),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    TextField(
                        value = "${address.calle} ${address.numero}, ${address.localidad}, ${address.region}",
                        onValueChange = {},
                        label = {
                            Text(
                                text = "Direccion",
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        enabled = false  // Deshabilitar la edición del TextField
                    )

                    IconButton(onClick = {
                        navController.navigate(
                            AppScreens.Location_Screen.createRoute(
                                "payment",
                                user.userId
                            )
                        )
                    }) {
                        Icon(
                            modifier = Modifier.size(36.dp),
                            painter = painterResource(id = R.drawable.address_add_location),
                            contentDescription = null,
                            tint = MaterialTheme.colors.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Medio de Pago",
                        style = MaterialTheme.typography.h6,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    paymentOptions.forEach { option ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    paymentMethod = option
                                }
                        ) {
                            RadioButton(
                                selected = paymentMethod == option,
                                onClick = { paymentMethod = option },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colors.primary,
                                    unselectedColor = MaterialTheme.colors.onSurface
                                )
                            )
                            Text(text = option, style = MaterialTheme.typography.body1)
                        }
                    }

                if (paymentMethod == "Tarjeta") {
                    Spacer(modifier = Modifier.height(16.dp))
                    cards.forEach { card ->
                        Text(
                            text = "**** **** **** ${
                                card.cardNumber.takeLast(4)
                            } - ${card.firstName} ${card.lastName}",
                            style = MaterialTheme.typography.body1,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        )
                        Text(
                            text = "Borrar tarjeta",
                            style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable {
                                    orderViewModel.user.tarjetas.remove(card)
                                    mainViewModel.updateUser(orderViewModel.user)
                                    navController.navigate("Payment");
                                }
                        )
                    }
                    Text(
                        text = "Añadir nueva tarjeta",
                        style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.primary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { navController.navigate("Card") }
                    )
                }

                    Spacer(modifier = Modifier.height(25.dp))

                    Text(
                        "Monto Sin envío: $totalAmount",
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal)
                    )
                    Text(
                        "Costo de envío: $deliveryFee",
                        style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Divider(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.5f),
                        thickness = 1.dp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Monto Total: $totalPayment",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.primary
                        ),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    Button(
                        onClick = { /* Manejar acción de pagar */ },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text("Pagar", fontSize = 18.sp)
                    }
                }
            }
        }

    }
}

@Composable
fun TopGroupPayment(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Cart_Screen.route) }) {
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

/*@Preview
@Composable
fun DefaultPreviewPayment() {
    val navController = rememberNavController()
    val viewModel= MainViewModel()
    val orderViewModel = OrderViewModel()

    PaymentScreen(navController, viewModel,orderViewModel)
}*/
