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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.RadioButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.CardViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun PaymentScreen(navController: NavHostController,viewModel: CardViewModel) {
    var totalAmount:Double=200.0
    var deliveryFee: Double=10.0
    var address by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Efectivo") }
    val paymentOptions = listOf("Efectivo", "Tarjeta")
    val totalPayment = totalAmount + deliveryFee


    AppScaffold(navController, "restaurant.name", null,
        { TopGroupPayment(navController, "restaurant.name") }){
        // Simulated list of preloaded cards
        Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                ,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Dirección de Entrega",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                BasicTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(Color.LightGray, shape = MaterialTheme.shapes.small)
                        .padding(16.dp)
                )

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
                    viewModel.cards.forEach { card ->
                        Text(
                            text = "**** **** **** ${card.cardNumber.toString().takeLast(4)} - ${card.firstName} ${card.lastName}",
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
                                    viewModel.removeCard(card);
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

                Text("Monto Sin envío: $totalAmount",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal))
                Text("Costo de envío: $deliveryFee",
                    style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Normal))
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
fun TopGroupPayment(navController: NavController, restaurantName: String) {
    val button_go_back =
        ButtonInterface(
            resourceId = R.drawable.go_back,
            imageDescription = "Go Back Icon",
            route = AppScreens.Home_Screen.route,

            )
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primarySurface)) {
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
        Spacer(modifier = Modifier.width(85.dp)) // Add space of 8.dp or any desired value

        Text(restaurantName,
            Modifier
                .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center)
    }
}
@Preview
@Composable
fun DefaultPreviewPayment() {
    val navController= rememberNavController()
    val cardViewModel= CardViewModel()

    PaymentScreen(navController,cardViewModel)
}
