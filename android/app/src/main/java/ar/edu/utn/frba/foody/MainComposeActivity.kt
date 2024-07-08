package ar.edu.utn.frba.foody

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.GroupDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.OrderDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.TokenDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.UserDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.GroupDataBase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.OrderDataBase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.RestaurantDataBase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.UserDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.AddressViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.GroupViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppNavigation
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import ar.edu.utn.frba.foody.ui.dataBase.FirebaseTokenService
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainComposeActivity : ComponentActivity() {
    lateinit var dbUserHelper: UserDataBase
    private lateinit var dbRestaurantHelper: RestaurantDataBase
    private lateinit var dbOrderHelper: OrderDataBase
    private lateinit var dbGroupHelper: GroupDataBase
    private lateinit var firebaseTokenManager: FirebaseTokenService
    private lateinit var userDataBaseFirebase: UserDataBaseFirebase
    private lateinit var orderDataBaseFirebase: OrderDataBaseFirebase
    private lateinit var groupDataBaseFirebase: GroupDataBaseFirebase
    private lateinit var tokenDataBaseFirebase: TokenDataBaseFirebase
    companion object{
        const val NOTIFICATION_CHANNEL_ID = "notification_fcm"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val initializationComplete =  mutableStateOf(false)
        requestNotificationPermissions()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
        lifecycleScope.launch(Dispatchers.IO) {
            initializeDatabase()
            withContext(Dispatchers.Main) {
                initializationComplete.value = true
            }
        }
        setContent {
            MyApp(initializationComplete.value)
        }
    }

    private fun requestNotificationPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val hasPermission = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.POST_NOTIFICATIONS
            )== PackageManager.PERMISSION_GRANTED

            if(!hasPermission){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                    0
                )
            }
        }
    }

    @Composable
    fun MyApp(initializationComplete: Boolean) {
        if (initializationComplete) {
            val navController = rememberNavController()
            val viewModel: MainViewModel = viewModel()
            val orderViewModel: OrderViewModel = viewModel()
            val groupViewModel: GroupViewModel = viewModel()
            LaunchedEffect(Unit) {
                orderViewModel.setServices(dbOrderHelper, orderDataBaseFirebase, navController)
                groupViewModel.setServices(dbGroupHelper, groupDataBaseFirebase, navController)
                viewModel.setServices(
                    userDataBaseFirebase,
                    tokenDataBaseFirebase,
                    navController,
                    firebaseTokenManager
                )
                viewModel.findAllUsers()

            }
            viewModel.user.observe(this@MainComposeActivity, Observer { user ->
                if (user != null) {
                    orderViewModel.user = user
                    orderViewModel.removeOrderFromSession()
                    orderViewModel.updateOrderLogin()
                    groupViewModel.userPrueba=user
                    navController.navigate(AppScreens.Home_Screen.route)
                    tokenDataBaseFirebase.addUserDeviceToken(firebaseTokenManager.getTokenFromPreferences()!!, user.userId)
                } else {
                    Toast.makeText(
                        navController.context,
                        "Incorrect User or Password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            AppNavigation(
                this@MainComposeActivity,
                navController,
                viewModel,
                orderViewModel,
                groupViewModel,
                dbUserHelper,
                dbRestaurantHelper,
                dbGroupHelper,
                dbOrderHelper,
                userDataBaseFirebase
            )

            val notification = intent.getStringExtra("notification")
            if (notification != null) {
                navController.navigate(AppScreens.Progress_Order_Screen.createRoute(""))
            }
        } else {
            //CircularProgressIndicator()
        }
    }

    private suspend fun initializeDatabase() = withContext(Dispatchers.IO) {
        dbUserHelper = UserDataBase(this@MainComposeActivity)
        //dbUserHelper.createDataBase(dbUserHelper)

        dbRestaurantHelper = RestaurantDataBase(this@MainComposeActivity)
        dbRestaurantHelper.deleteAndCreateTables(dbUserHelper)

        dbOrderHelper = OrderDataBase(this@MainComposeActivity)
        //dbOrderHelper.deleteAndCreateTables()

        dbGroupHelper = GroupDataBase(this@MainComposeActivity)
        dbGroupHelper.createDataBase(dbGroupHelper)

        createTestData(dbRestaurantHelper)

        //Get firebase token for this device
        firebaseTokenManager = FirebaseTokenService(this@MainComposeActivity)
        var firebaseToken = firebaseTokenManager.getTokenFromPreferences()
        if (firebaseToken.isNullOrEmpty()) {
            firebaseTokenManager.getAndSaveToken()
        }

        //Create instance
        val database = FirebaseDatabase.getInstance()

        //Create Firebase data base instance
         userDataBaseFirebase = UserDataBaseFirebase(database)
        orderDataBaseFirebase = OrderDataBaseFirebase(database)
        groupDataBaseFirebase = GroupDataBaseFirebase(database)
        tokenDataBaseFirebase = TokenDataBaseFirebase(database)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Notificaciones de FCM",
                NotificationManager.IMPORTANCE_HIGH,
            )
            channel.description = "Estas notificaciones van a ser recibidas desde FCM"
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}