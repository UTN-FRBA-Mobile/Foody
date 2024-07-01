package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.TokenDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.UserDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.FirebaseTokenService
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import com.google.firebase.Firebase

class MainViewModel() : ViewModel() {
    var userDataBaseFirebase: UserDataBaseFirebase? = null
    var tokenDataBaseFirebase: TokenDataBaseFirebase? = null
    var navController: NavController? = null
    var firebaseTokenService: FirebaseTokenService? = null


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

    fun setServices(userDataBaseFirebase: UserDataBaseFirebase, tokenDataBaseFirebase: TokenDataBaseFirebase, navController: NavController, firebaseTokenService: FirebaseTokenService) {
        this.userDataBaseFirebase = userDataBaseFirebase
        this.tokenDataBaseFirebase = tokenDataBaseFirebase
        this.navController = navController
        this.firebaseTokenService = firebaseTokenService
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

    fun logout() {
        navController!!.navigate(AppScreens.Login_Screen.route)
        var token = firebaseTokenService!!.getTokenFromPreferences()!!
        tokenDataBaseFirebase!!.deleteUserDeviceToken(userId = user.value!!.userId, token = token)
        //TODO sacar todo del stack de navegación
    }
}
