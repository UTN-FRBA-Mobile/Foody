package ar.edu.utn.frba.foody.ui.navigation

sealed class AppScreens(val route: String) {
    object Session_Screen : AppScreens("Session")
    object Splash_Screen : AppScreens("Splash")
    object Home_Screen: AppScreens("Home")
    object Login_Screen: AppScreens("Login")
    object SignUp_Screen: AppScreens("SignUp")
    //object Location_Screen: AppScreens("Location")
    object Location_Screen : AppScreens("location_screen/{origin}/{id}") {
        fun createRoute(origin: String,id:String) = "location_screen/$origin/$id"
    }
    object Profile_Screen: AppScreens("Profile")
    object Cart_Screen: AppScreens("cart_screen/{origin}") {
        fun createRoute(origin: String) = "cart_screen/$origin"
    }
    object Orders_Screen: AppScreens("Orders")
    object Restaurant_Screen: AppScreens("Restaurant")
    object Order_Screen: AppScreens("Order/{order_id}/{origin}"){
        fun createRoute(order_id:String,origin: String) = "Order/$order_id/$origin"
    }
    object Progress_Order_Screen: AppScreens("Progress Order/{order_id}"){
        fun createRoute(order_id: String) = "Progress Order/$order_id"
    }
    object Group_Screen: AppScreens("Group")
    object Create_Group_Screen: AppScreens("Create Group")
    object Join_Group_Screen: AppScreens("Join Group")
    object Card_Screen: AppScreens("Card")
    object Payment: AppScreens("Payment")
    object PendingOrder: AppScreens("PendingOrder")
    object OrdersDelivered: AppScreens("ordersDelivered")
    object OnTheWayOrders: AppScreens("onTheWayOrders")
}