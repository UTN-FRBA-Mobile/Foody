package ar.edu.utn.frba.foody.ui.Classes

import ar.edu.utn.frba.foody.R

data class Dish(
        val dishId: Int =0,
        val name: String,
        val description: String,
        val price: Double,
        val imageResourceId: Int,
        val restaurantId: Int
)