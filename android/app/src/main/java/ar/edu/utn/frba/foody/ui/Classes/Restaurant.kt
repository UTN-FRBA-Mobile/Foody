package ar.edu.utn.frba.foody.ui.Classes

class Restaurant {
    data class RestaurantInfo(
        val restaurantId: Int = 0,
        val name: String = "",
        val imageDescription: String = "",
        val image: Int = 0,
        val dishes: List<Dish.DishInfo> = emptyList()
    )
}