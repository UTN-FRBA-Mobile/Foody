package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens


@Composable
fun RestaurantScreen(navController: NavHostController, viewModel: MainViewModel, orderViewModel: OrderViewModel) {
    var restaurant = viewModel.getPickedRestaurant()
    val userOrder = orderViewModel.getUserOrder(restaurant)
    AppScaffold(navController, restaurant.name, {BottomGroupRestaurant(navController)},
        { TopGroupRestaurant(navController, restaurant.name) }){
        DishesGrid(navController = navController, orderViewModel, restaurant.dishes, userOrder)
    }
}

@Composable
fun TopGroupRestaurant(navController: NavController, restaurantName: String) {
    val button_go_back =
        ButtonInterface(
            resourceId = R.drawable.go_back,
            imageDescription = "Go Back Icon",
            route = AppScreens.Home_Screen.route,

            )
    val button_cart=ButtonInterface(
        resourceId = R.drawable.cart_icon,
        imageDescription = "Cart Icon",
        route = AppScreens.Cart_Screen.route,
    )
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primarySurface),
        horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(
            onClick = { navController.navigate(button_go_back.route) },
        ) {
            Image(
                painter = painterResource(id = button_go_back.resourceId),
                contentDescription = button_go_back.imageDescription,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.FillBounds
            )
        }
        Text(restaurantName,
            Modifier
            .align(Alignment.CenterVertically),
            textAlign = TextAlign.Center)


        IconButton(
            onClick = { navController.navigate(button_cart.route) },
        ) {
            Image(
                painter = painterResource(id = button_cart.resourceId),
                contentDescription = button_cart.imageDescription,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

@Composable
fun BottomGroupRestaurant(navController: NavController) {
    val buttons = listOf(
        ButtonInterface(
            resourceId = R.drawable.user_icon,
            imageDescription = "User Icon",
            route = AppScreens.Profile_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            route = AppScreens.Orders_Screen.route,
        )
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        buttons.forEach{
            IconButton(
                onClick = { navController.navigate(it.route) },
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    painter = painterResource(id = it.resourceId),
                    contentDescription = it.imageDescription,
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    }
}

@Composable
fun DishesGrid(navController: NavController, viewModel: OrderViewModel, dishes: List<Dish>, userOrder: UserOrder) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(dishes.size) { index ->
            DishCard(navController, viewModel, dishes[index], userOrder, userOrder.items.firstOrNull { x -> x.dish.dishId == dishes[index].dishId } )
        }
    }
}

@Composable
fun DishCard(navController: NavController, viewModel: OrderViewModel, dish: Dish, userOrder: UserOrder, userOrderItemInfo: OrderItemInfo?) {
    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .height(300.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { /* Navigate to restaurant details */ }
        ) {
            Image(
                painter = painterResource(id = dish.imageResourceId),
                contentDescription = dish.name,
                modifier = Modifier
                    .height(80.dp)
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = dish.name,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.padding(vertical = 4.dp)
            )
            Text(
                text = dish.description,
                style = MaterialTheme.typography.body2
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = { viewModel.changeItemQuantityIfExists(userOrder.userOrderId, userOrderItemInfo, 1, dish) }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "Increase")
                }

                Text(text = (userOrderItemInfo?.quantity ?: 0).toString(), fontSize = 18.sp)

                IconButton(onClick = { viewModel.changeItemQuantityIfExists(userOrder.userOrderId, userOrderItemInfo, -1, dish) }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Decrease")
                }
            }
        }
    }
}


@Preview
@Composable
fun DefaultPreviewRestaurant() {
    val navController= rememberNavController()
    val viewModel = MainViewModel()
    //RestaurantScreen(navController, viewModel) TODO: arreglar esto
}
