package ar.edu.utn.frba.foody.ui.Classes

import ar.edu.utn.frba.foody.R

class Dish {
    data class DishInfo(
    val dishId: Int,
    val name: String,
    val description: String,
    val imageResourceId: Int

    )
    fun getDishes(): List<DishInfo> {
        return listOf(
            DishInfo(1,
                "Pizza",
                "Pizza peperonis",
                R.drawable.pizza_de_peperoni),
            DishInfo(2,
                "Hamburger",
                "1 burger, 1 cheese, 2 lettuce,1 tomato",
                R.drawable.hamburger)
        )
    }

}