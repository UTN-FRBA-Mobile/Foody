package ar.edu.utn.frba.foody.ui.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish

@Composable
fun DishAlert(show: Boolean, dish: Dish, totalPrice: Double?, onDismiss: () -> Unit) {
    val priceText = if (totalPrice != null) "Total Price: $$totalPrice" else "Price: $${dish.price}"

    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            buttons = {},
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.width(170.dp)) {
                        Text(text = dish.name, style = MaterialTheme.typography.h6)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = dish.description, style = MaterialTheme.typography.body2)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = priceText, style = MaterialTheme.typography.body2)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Image(
                        painter = painterResource(id = dish.imageResourceId),
                        contentDescription = dish.name,
                        modifier = Modifier.size(64.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    /* DishAlert(
         show = true,
         dish = Dish(
             dishId = 4,
             name = "Tuna Roll",
             description = "Tuna roll with avocado",
             imageResourceId = R.drawable.tuna_roll,
             price = 5000.0,
             restaurantId = 2
         ),
         onDismiss = {}
     )*/
    DishAlert(
        show = true,
        dish =
        Dish(
            dishId = 3,
            name = "Salmon Nigiri",
            description = "Fresh salmon on sushi rice",
            imageResourceId = R.drawable.salmon_nigiri,
            price = 5000.0,
            restaurantId = 2
        ),
        null,
        onDismiss = {}
    )
    /*DishAlert(
        show = true,
        dish = Dish(
            dishId = 10,
            name = "Sweet Potato Fries",
            description = "Crispy sweet potato fries",
            imageResourceId = R.drawable.sweet_potato_fries,
            price = 5000.0,
            restaurantId = 5
        ),
        onDismiss = {}
    )*/
}
