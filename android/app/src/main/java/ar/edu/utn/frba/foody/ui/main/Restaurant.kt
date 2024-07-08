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
import androidx.compose.material.BottomAppBar
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.Restaurant
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.composables.DishAlert
import ar.edu.utn.frba.foody.ui.composables.shimmerBrush
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens


@Composable
fun RestaurantScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    orderViewModel: OrderViewModel
) {
    val loading = remember { mutableStateOf(true) }
    val restaurant = viewModel.getPickedRestaurant()
    val order = orderViewModel.getPickedOrder()
    val userOrder = orderViewModel.getUserOrder(restaurant)

    AppScaffold(
        { BottomGroupRestaurant(navController, order) },
        { TopGroupRestaurant(navController) }
    ) {
        DishesGrid(orderViewModel, restaurant.dishes, userOrder, restaurant, loading.value)
    }
}

@Composable
fun TopGroupRestaurant(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Home_Screen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.go_back),
                    contentDescription = "Go Back Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    )
}

@Composable
fun BottomGroupRestaurant(navController: NavController, order: Order) {
    val buttons = mutableListOf(
        ButtonInterface(
            resourceId = R.drawable.cart_icon,
            imageDescription = "Cart Icon",
            route = AppScreens.Cart_Screen.createRoute(origin = "restaurant"),
        )
    )

    if (order.group == null) {
        buttons.add(
            ButtonInterface(
                resourceId = R.drawable.create_group_icon,
                imageDescription = "Create Group Icon",
                route = AppScreens.Create_Group_Screen.route
            )
        )
    }

    BottomAppBar {
        Row(modifier = Modifier.fillMaxWidth()) {
            buttons.forEach {
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
}

@Composable
fun DishesGrid(
    viewModel: OrderViewModel,
    dishes: List<Dish>,
    userOrder: UserOrder,
    restaurant: Restaurant,
    loading: Boolean,
) {
    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Text(
        text = restaurant.name,
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(top = 16.dp)
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth().padding(top = 48.dp)
    ) {
        items(dishes.size) { index ->
            DishCard(
                viewModel,
                dishes[index],
                userOrder.items.firstOrNull { x -> x.dish.dishId == dishes[index].dishId },
                loading
            )
        }
    }
}

@Composable
fun DishCard(
    viewModel: OrderViewModel,
    dish: Dish,
    userOrderItemInfo: OrderItemInfo?,
    loading: Boolean
) {
    val showDialog = remember { mutableStateOf(false) }

    DishAlert(show = showDialog.value, dish = dish, null) {
        showDialog.value = false
    }

    Card(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxSize()
            .height(200.dp)
            .background(shimmerBrush(showShimmer = loading)),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable { showDialog.value = true }
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
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    viewModel.changeItemQuantityIfExists(
                        userOrderItemInfo,
                        1,
                        dish)
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "Increase"
                    )
                }
                Text(text = (userOrderItemInfo?.quantity ?: 0).toString(), fontSize = 18.sp)
                IconButton(onClick = {
                    viewModel.changeItemQuantityIfExists(
                        userOrderItemInfo,
                        -1,
                        dish
                    )
                }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Decrease"
                    )
                }
            }
        }
    }
}


/*@Preview
@Composable
fun DefaultPreviewRestaurant() {
    val navController = rememberNavController()
    val viewModel = MainViewModel()
    val orderViewModel = OrderViewModel()
    RestaurantScreen(navController, viewModel, orderViewModel)
}*/
