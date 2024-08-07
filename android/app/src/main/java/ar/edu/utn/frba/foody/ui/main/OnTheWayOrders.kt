package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Status
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel

@Composable
fun OrdersOnTheWayScreen(navController: NavController, viewModel: OrderViewModel) {
    viewModel.findOrdersDeliveredById()
    val orders = viewModel.getAllOrdersDeliveredById()
    AppScaffold(
        null,
        { TopGroupOrderListDelivered(navController = navController)}
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_signup),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
                .fillMaxWidth()
            ) {
                Text(text = "Ordenes En Camino",
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
                orders.forEach { order ->
                    item {
                        if (viewModel.hasItems(order)) {
                            if (order.status.equals(Status.ONTHEWAY)) {
                                OrderItemDelivered(
                                    navController = navController,
                                    order = order,
                                    title = "ordersOnTheWay"
                                )
                            }
                        }
                    }
                }
                if (orders.isNotEmpty()) {
                    item {
                        Divider()
                    }
                }
            }
        }
    }
}