package ar.edu.utn.frba.foody.ui.Classes

import ar.edu.utn.frba.foody.R

class Dish {
    data class DishInfo(
    val dishId: Int,
    val name: String,
    val description: String,
    val imageResourceId: Int
    )
}