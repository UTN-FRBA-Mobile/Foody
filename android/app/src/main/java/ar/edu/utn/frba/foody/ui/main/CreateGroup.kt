package ar.edu.utn.frba.foody.ui.main

import android.content.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import java.util.UUID

@Composable
fun CreateGroupScreen(navController: NavController, orderViewModel: OrderViewModel, context: Context) {
    val order = orderViewModel.getPickedOrder()
    var groupName by remember { mutableStateOf("") }
    val baseUrl = context.getString(R.string.base_url)
    val groupId = UUID.randomUUID().toString()
    val finalUrl = "$baseUrl/$groupId"

    val shareIntent = remember {
        Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, finalUrl)
        }
    }

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupCreateGroup(navController = navController) }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 32.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            CustomText(text = "Create Group")
        }
        Box(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 100.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextField(
                    value = groupName,
                    onValueChange = { groupName = it },
                    label = {
                        Text(text = "Group Name")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                CustomText(text = order.restaurant.name)
                CustomText(text = order.direction)
                CustomText(text = "(You pay the total)")
            }
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .fillMaxWidth()
                .padding(bottom = 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { context.startActivity(Intent.createChooser(shareIntent, null)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Create", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun CustomText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.h5,
        color = MaterialTheme.colors.primary,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp)
    )
}

@Composable
fun TopGroupCreateGroup(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Cart_Screen.route) }) {
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

@Preview
@Composable
fun CreateGroupPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()
    val orderViewModel = OrderViewModel()
    CreateGroupScreen(navController = navController, orderViewModel = orderViewModel, context = context)
}