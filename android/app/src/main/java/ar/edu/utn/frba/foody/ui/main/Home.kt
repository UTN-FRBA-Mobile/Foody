package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
    AppScaffold(navController) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Cyan)) {
            Box(modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .background(color = Color.White)
            ) {
                    Text(text = "Restaurants",
                        modifier = Modifier
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Divider()
                repeat(8) {
                    RestaurantItem(navController)
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Box(modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)) {
                ButtonGroup(navController)
            }
        }
    }

}

@Composable
fun RestaurantItem(navController: NavController) {
    Row (modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = { navController.navigate(AppScreens.Restaurant_Screen.route) })
        .padding(16.dp, 4.dp)) {
        Image(
            painter = painterResource(id = R.drawable.restaurant),
            contentDescription = "Restaurant Image",
            modifier = Modifier.size(128.dp, 64.dp),
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

data class ButtonInterface(
    val resourceId: Int,
    val imageDescription: String,
    val route: String
)

@Composable
fun ButtonGroup(navController: NavController) {
    val buttons = listOf(
        ButtonInterface(
            resourceId = R.drawable.user_icon,
            imageDescription = "User Icon",
            route = AppScreens.Login_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.cart_icon,
            imageDescription = "Cart Icon",
            route = AppScreens.Login_Screen.route,
        ),
        ButtonInterface(
            resourceId = R.drawable.order_icon,
            imageDescription = "Order Icon",
            route = AppScreens.Login_Screen.route,
        )
    )

    Row(modifier = Modifier.fillMaxWidth()) {
        buttons.forEach{
            OutlinedButton(
                onClick = { navController.navigate(it.route) },
                shape = RoundedCornerShape(0.dp),
                contentPadding = PaddingValues(10.dp),
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

@Preview
@Composable
fun DefaultPreview() {
    val navController= rememberNavController()
    HomeScreen(navController)
}
