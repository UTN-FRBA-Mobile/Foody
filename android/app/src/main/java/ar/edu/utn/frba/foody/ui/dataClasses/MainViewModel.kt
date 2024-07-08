package ar.edu.utn.frba.foody.ui.dataClasses

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.TokenDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.UserDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.FirebaseTokenService
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import androidx.compose.runtime.State

class MainViewModel() : ViewModel() {
    var userDataBaseFirebase: UserDataBaseFirebase? = null
    var tokenDataBaseFirebase: TokenDataBaseFirebase? = null
    var navController: NavController? = null
    var firebaseTokenService: FirebaseTokenService? = null
    var allUsers = mutableListOf<User>()
    private var restaurant by mutableStateOf(Restaurant())
        private set
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user
    private val _email = mutableStateOf("")
    val email: State<String> = _email
    private val _password = mutableStateOf("")
    val password: State<String> = _password
    private val _contactNumber = mutableStateOf("")
    val contactNumber: State<String> = _contactNumber
    private val _delivery = mutableStateOf("No")
    val delivery: State<String> = _delivery
    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }
    fun updateContactNumber(newContactNumber: String) {
        _contactNumber.value = newContactNumber
    }
    fun updateDelivery(newDelivery: String) {
        _delivery.value = newDelivery
    }
    fun clearSignUpFields() {
        _email.value = ""
        _password.value = ""
        _contactNumber.value = ""
        _delivery.value = "No"
    }
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
    fun validateUser(email: String): Boolean{
       return allUsers.any { user->user.userId==email }
    }
    fun findAllUsers(){
        userDataBaseFirebase!!.getAllUsers(){
            users ->
            if (users.isNotEmpty()) allUsers= users.toMutableList()
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
    }
}
