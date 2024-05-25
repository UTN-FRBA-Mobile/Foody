package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.UiString

class MainViewModel() : ViewModel() {
    private var restaurant by mutableStateOf(Restaurant.RestaurantInfo())
        private set

    fun updateRestaurant(newRestaurant: Restaurant.RestaurantInfo) {
        restaurant = newRestaurant
    }

    fun getPickedRestaurant(): Restaurant.RestaurantInfo {
        return restaurant
    }

    val restaurants: List<Restaurant.RestaurantInfo> = listOf(
        Restaurant.RestaurantInfo(
            restaurantId = 1,
            name = "La Bella Italia",
            imageDescription = "A cozy Italian restaurant",
            image = R.drawable.italian_restaurant,
            dishes = listOf(
                Dish.DishInfo(
                    dishId = 1,
                    name = "Spaghetti Carbonara",
                    description = "Classic Italian pasta",
                    imageResourceId = R.drawable.spaghetti_carbonara
                ),
                Dish.DishInfo(
                    dishId = 2,
                    name = "Margherita Pizza",
                    description = "Pizza with tomatoes, mozzarella, and basil",
                    imageResourceId = R.drawable.peperoni_pizza
                )
            )
        ),
        Restaurant.RestaurantInfo(
            restaurantId = 2,
            name = "Sushi World",
            imageDescription = "Fresh sushi and sashimi",
            image = R.drawable.sushi_restaurant,
            dishes = listOf(
                Dish.DishInfo(
                    dishId = 3,
                    name = "Salmon Nigiri",
                    description = "Fresh salmon on sushi rice",
                    imageResourceId = R.drawable.salmon_nigiri
                ),
                Dish.DishInfo(
                    dishId = 4,
                    name = "Tuna Roll",
                    description = "Tuna roll with avocado",
                    imageResourceId = R.drawable.tuna_roll
                )
            )
        ),
        Restaurant.RestaurantInfo(
            restaurantId = 3,
            name = "Curry House",
            imageDescription = "Parrilla 'El Argentino'",
            image = R.drawable.argentinian_restaurant,
            dishes = listOf(
                Dish.DishInfo(
                    dishId = 5,
                    name = "Tira de asado",
                    description = "Tira de asado con sal",
                    imageResourceId = R.drawable.tira_asado
                ),
                Dish.DishInfo(
                    dishId = 6,
                    name = "Vacío a la provenzal",
                    description = "Vacío a la provenzal",
                    imageResourceId = R.drawable.vacio
                )
            )
        ),
        Restaurant.RestaurantInfo(
            restaurantId = 4,
            name = "Taco Fiesta",
            imageDescription = "Mexican street food",
            image = R.drawable.taco_restaurant,
            dishes = listOf(
                Dish.DishInfo(
                    dishId = 7,
                    name = "Carne Asada Tacos",
                    description = "Grilled beef tacos",
                    imageResourceId = R.drawable.carne_asada_tacos
                ),
                Dish.DishInfo(
                    dishId = 8,
                    name = "Guacamole and Chips",
                    description = "Fresh guacamole with tortilla chips",
                    imageResourceId = R.drawable.guacamole_and_chips
                )
            )
        ),
        Restaurant.RestaurantInfo(
            restaurantId = 5,
            name = "Burger Haven",
            imageDescription = "Gourmet burgers and fries",
            image = R.drawable.burger_restaurant,
            dishes = listOf(
                Dish.DishInfo(
                    dishId = 9,
                    name = "Cheeseburger",
                    description = "Beef burger with cheese, lettuce, and tomato",
                    imageResourceId = R.drawable.cheeseburger
                ),
                Dish.DishInfo(
                    dishId = 10,
                    name = "Sweet Potato Fries",
                    description = "Crispy sweet potato fries",
                    imageResourceId = R.drawable.sweet_potato_fries
                )
            )
        )
    )
}
