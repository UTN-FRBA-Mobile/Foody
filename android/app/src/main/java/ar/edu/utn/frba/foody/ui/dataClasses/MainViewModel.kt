package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.UserDataBaseFirebase
import com.google.firebase.Firebase

class MainViewModel() : ViewModel() {
    var userDataBaseFirebase: UserDataBaseFirebase? = null


    private var restaurant by mutableStateOf(Restaurant())
        private set

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun fetchUserByEmail(email: String, password: String) {
        userDataBaseFirebase!!.getUserByEmail(email) { user ->
            if(user != null && user.password == password) {
                _user.postValue(user)
            }
            else {
                _user.postValue(null)
            }
        }
    }

    fun setDataBase(userDataBaseFirebase: UserDataBaseFirebase) {
        this.userDataBaseFirebase = userDataBaseFirebase
    }

    fun updateRestaurant(newRestaurant: Restaurant) {
        restaurant = newRestaurant
    }

    fun getPickedRestaurant(): Restaurant {
        return restaurant
    }

    fun getPickedRestaurantName(): String {
        return restaurant.name
    }

    fun updateUser(user: User) {
        userDataBaseFirebase!!.updateUser(user)
    }

    //TODO: pasar los datos estáticos a otro lado
    val restaurants: List<Restaurant> = listOf(
        Restaurant(
            restaurantId = 1,
            name = "La Bella Italia",
            imageDescription = "A cozy Italian restaurant",
            image = R.drawable.italian_restaurant,
            dishes = listOf(
                Dish(
                    dishId = 1,
                    name = "Spaghetti Carbonara",
                    description = "Classic Italian pasta",
                    imageResourceId = R.drawable.spaghetti_carbonara,
                    price = 5000.0,
                    restaurantId = 1
                ),
                Dish(
                    dishId = 2,
                    name = "Margherita Pizza",
                    description = "Pizza with tomatoes, mozzarella, and basil",
                    imageResourceId = R.drawable.peperoni_pizza,
                    price = 8000.0,
                    restaurantId = 1
                )
            )
        ),
        Restaurant(
            restaurantId = 2,
            name = "Sushi World",
            imageDescription = "Fresh sushi and sashimi",
            image = R.drawable.sushi_restaurant,
            dishes = listOf(
                Dish(
                    dishId = 3,
                    name = "Salmon Nigiri",
                    description = "Fresh salmon on sushi rice",
                    imageResourceId = R.drawable.salmon_nigiri,
                    price = 5000.0,
                    restaurantId = 2
                ),
                Dish(
                    dishId = 4,
                    name = "Tuna Roll",
                    description = "Tuna roll with avocado",
                    imageResourceId = R.drawable.tuna_roll,
                    price = 5000.0,
                    restaurantId = 2
                )
            )
        ),
        Restaurant(
            restaurantId = 3,
            name = "Curry House",
            imageDescription = "Parrilla 'El Argentino'",
            image = R.drawable.argentinian_restaurant,
            dishes = listOf(
                Dish(
                    dishId = 5,
                    name = "Tira de asado",
                    description = "Tira de asado con sal",
                    imageResourceId = R.drawable.tira_asado,
                    price = 5000.0,
                    restaurantId = 3
                ),
                Dish(
                    dishId = 6,
                    name = "Vacío a la provenzal",
                    description = "Vacío a la provenzal",
                    imageResourceId = R.drawable.vacio,
                    price = 5000.0,
                    restaurantId = 3
                )
            )
        ),
        Restaurant(
            restaurantId = 4,
            name = "Taco Fiesta",
            imageDescription = "Mexican street food",
            image = R.drawable.taco_restaurant,
            dishes = listOf(
                Dish(
                    dishId = 7,
                    name = "Carne Asada Tacos",
                    description = "Grilled beef tacos",
                    imageResourceId = R.drawable.carne_asada_tacos,
                    price = 5000.0,
                    restaurantId = 4
                ),
                Dish(
                    dishId = 8,
                    name = "Guacamole and Chips",
                    description = "Fresh guacamole with tortilla chips",
                    imageResourceId = R.drawable.guacamole_and_chips,
                    price = 5000.0,
                    restaurantId = 4
                )
            )
        ),
        Restaurant(
            restaurantId = 5,
            name = "Burger Haven",
            imageDescription = "Gourmet burgers and fries",
            image = R.drawable.burger_restaurant,
            dishes = listOf(
                Dish(
                    dishId = 9,
                    name = "Cheeseburger",
                    description = "Beef burger with cheese, lettuce, and tomato",
                    imageResourceId = R.drawable.cheeseburger,
                    price = 5000.0,
                    restaurantId = 5
                ),
                Dish(
                    dishId = 10,
                    name = "Sweet Potato Fries",
                    description = "Crispy sweet potato fries",
                    imageResourceId = R.drawable.sweet_potato_fries,
                    price = 5000.0,
                    restaurantId = 5
                )
            )
        )
    )
}
