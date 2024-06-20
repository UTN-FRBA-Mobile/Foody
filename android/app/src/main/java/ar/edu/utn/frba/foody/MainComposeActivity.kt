package ar.edu.utn.frba.foody

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.NonNull
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.GroupDataBase
import ar.edu.utn.frba.foody.ui.dataBase.OrderDataBase
import ar.edu.utn.frba.foody.ui.dataBase.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.UserDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.AddressViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.CardViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppNavigation
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class MainComposeActivity : ComponentActivity() {
    public lateinit var dbUserHelper: UserDataBase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbUserHelper = UserDataBase(this)
       // dbUserHelper.createDataBase(dbUserHelper)

        val dbRestaurantHelper = RestaurantDataBase(this)
        //dbRestaurantHelper.deleteAndCreateTables(dbUserHelper)

        val dbOrderHelper = OrderDataBase(this)
        //dbOrderHelper.deleteAndCreateTables()

        //createTestData(dbRestaurantHelper)
        val dbGroupHelper = GroupDataBase(this)
        dbGroupHelper.createDataBase(dbGroupHelper)

        createTestData(dbRestaurantHelper)

        //Create instance
        val database = FirebaseDatabase.getInstance()

        //Get reference of table users
        val ref = database.getReference("users")

        val userExample = User(userId = 0, email = "example@.gmail.com")
        val userExample1 = User(userId = 1, email = "johndoe@.gmail.com")

        //Insert users
        ref.child(userExample.userId.toString()).setValue(userExample)
        ref.child(userExample1.userId.toString()).setValue(userExample1)

        //Get reference of particular user by id
        val childRef = ref.child(userExample.userId.toString())

        //Get user by id
        childRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(@NonNull dataSnapshot: DataSnapshot) {
                // Obtiene el objeto Usuario para el usuario específico
                val usuario: User? = dataSnapshot.getValue(User::class.java)
                if (usuario != null) {
                    // Aquí puedes trabajar con el objeto Usuario obtenido
                    val email = usuario.email
                    // Puedes imprimirlos, mostrarlos en tu UI, etc.
                    Log.d("Usuario", "Email: $email")
                } else {
                    Log.d("Usuario", "No se encontró el usuario con ID: ${userExample.userId}")
                }
            }

            override fun onCancelled(@NonNull databaseError: DatabaseError) {
                // Maneja errores de lectura aquí
                Log.e(
                    "Firebase",
                    "Error al leer usuario con ID: ${userExample.userId}",
                    databaseError.toException()
                )
            }
        })

        //Empty user table
        /*ref.removeValue()
            .addOnSuccessListener(OnSuccessListener<Void?> { // Operación de eliminación exitosa
                Log.d("Firebase", "Nodo vaciado correctamente")
            })
            .addOnFailureListener(OnFailureListener { e -> // Manejo de errores
                Log.e("Firebase", "Error al vaciar el nodo", e)
            })*/

        setContent {
            val navController = rememberNavController()
            val viewModel = viewModel<MainViewModel>()
            val orderViewModel = viewModel<OrderViewModel>()
            val cardViewModel = viewModel<CardViewModel>()
            val groupViewModel = viewModel<GroupViewModel>()
            val addressViewModel = viewModel<AddressViewModel>()
            orderViewModel.setDatabase(dbOrderHelper)
            groupViewModel.setDatabase(dbGroupHelper)
            AppNavigation(
                this, navController, viewModel, orderViewModel, cardViewModel, groupViewModel,
                addressViewModel, dbUserHelper, dbRestaurantHelper, dbGroupHelper, dbOrderHelper
            )
        }
    }

    fun createTestData(dbRestaurantHelper: RestaurantDataBase) {
        val restaurant1 = Restaurant(
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
        )

        val restaurant2 = Restaurant(
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
        )

        val restaurant3 = Restaurant(
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
        )

        val restaurant4 = Restaurant(
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
        )

        val restaurant5 = Restaurant(
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

        dbRestaurantHelper.insertRestaurant(restaurant1, dbUserHelper)
        dbRestaurantHelper.insertRestaurant(restaurant2, dbUserHelper)
        dbRestaurantHelper.insertRestaurant(restaurant3, dbUserHelper)
        dbRestaurantHelper.insertRestaurant(restaurant4, dbUserHelper)
        dbRestaurantHelper.insertRestaurant(restaurant5, dbUserHelper)
    }

    /*
           val orderItems1 = listOf(OrderItemInfo(dish = dish1, quantity = 2, id = 1), OrderItemInfo(dish = dish2, quantity = 1, id = 2))
           val orderItems2 = listOf(OrderItemInfo(dish = dish2, quantity = 3, id = 2), OrderItemInfo(dish = dish1, quantity = 1, id = 3))
           val orderItems3 = listOf(OrderItemInfo(dish = dish2, quantity = 2, id = 3))
           val orderItems4 = listOf(OrderItemInfo(dish = dish1, quantity = 4, id = 4), OrderItemInfo(dish = dish2, quantity = 2, id = 1))
           val orderItems5 = listOf(OrderItemInfo(dish = dish1, quantity = 1, id = 5))


           val userOrder1 = UserOrder(userOrderId = 1, items = orderItems1, user = user1)
           val userOrder2 = UserOrder(userOrderId = 2, items = orderItems2, user = user2)
           val userOrder3 = UserOrder(userOrderId = 3, items = orderItems3, user = user3)
           val userOrder4 = UserOrder(userOrderId = 4, items = orderItems4, user = user4)
           val userOrder5 = UserOrder(userOrderId = 5, items = orderItems5, user = user5)

    val group = Group(
        groupId = 0,
        name = "Los Movileros",
        members = mutableListOf(
            User(
                userId = 0,
                userName = "Zeke",
                userPassword = "SeQueL"
            ),
            User(
                userId = 1,
                userName = "Walter",
                userPassword = "1234"
            )
        ),
        limit = 10,
    )


           val order1 = Order(orderId = 123, name = "Sample Order", restaurant = restaurant, userOrders = listOf(userOrder1, userOrder2, userOrder3, userOrder4, userOrder5))
           viewModel.updateOrder(order1)
    val order1 = Order(
        orderId = 123,
        name = "Sample Order",
        restaurant = restaurant,
        userOrders = listOf(userOrder1, userOrder2, userOrder3, userOrder4, userOrder5),
        group = group
    )
    viewModel.updateOrder(order1)

     */
}