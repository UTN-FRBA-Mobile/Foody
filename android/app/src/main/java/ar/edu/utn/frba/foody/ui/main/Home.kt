package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun HomeScreen(
    navController: NavController) {
    AppScaffold(navController, null, { BottomGroupHome(navController) },{ TopGroupHome(navController)}) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                ) {
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 32.dp)
                .fillMaxWidth()
            ) {
                    Text(text = "Restaurants",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(505.dp)
            ) {
                repeat(7) {
                    item {
                        RestaurantItem(navController)

                    }
                }
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
            }
        }
    }

}

@Composable
fun RestaurantItem(navController: NavController) {
    Card(backgroundColor = MaterialTheme.colors.secondary) {
        Row (modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = { navController.navigate(AppScreens.Restaurant_Screen.route) })
            .padding(16.dp, 4.dp)) {
            Image(
                painter = painterResource(id = R.drawable.restaurant),
                contentDescription = "Restaurant Image",
                modifier = Modifier
                    .size(128.dp, 64.dp)
                    .clip(shape = RoundedCornerShape(10.dp)),
                contentScale = ContentScale.FillBounds,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Restaurant 1",
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically),
                textAlign = TextAlign.Center
            )
        }
        Divider()
    }
}

data class ButtonInterface(
    val resourceId: Int,
    val imageDescription: String,
    val route: String
)


@Composable
fun BottomGroupHome(navController: NavController) {
    val buttons = listOf(
        ButtonInterface(
            resourceId = R.drawable.user_icon,
            imageDescription = "User Icon",
            route = AppScreens.Profile_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.cart_icon,
            imageDescription = "Cart Icon",
            route = AppScreens.Cart_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            route = AppScreens.Order_Screen.route,
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
fun TopGroupHome(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Login_Screen.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.logout_icon),
                    contentDescription = "Logout Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    )
}
@Preview
@Composable
fun DefaultPreview() {
    val navController= rememberNavController()
    HomeScreen(navController)
}
