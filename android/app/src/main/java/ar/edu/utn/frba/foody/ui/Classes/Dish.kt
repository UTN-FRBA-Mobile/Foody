package ar.edu.utn.frba.foody.ui.Classes

data class Dish(
        val dishId: Int = 0,
        val name: String = "",
        val description: String = "",
        val price: Double = 0.0,
        val imageResourceId: Int = 0,
        val restaurantId: Int = 0
)