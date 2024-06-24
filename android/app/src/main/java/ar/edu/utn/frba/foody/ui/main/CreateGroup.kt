package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.GroupDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.*
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun CreateGroupScreen(
    navController: NavController,
    orderViewModel: OrderViewModel,
    groupViewModel: GroupViewModel,
    groupDataBase: GroupDataBase
) {
    val order = orderViewModel.getPickedOrder()
    val group = Group()
    var groupName by remember { mutableStateOf("") }
    var password by remember {
        mutableStateOf("")
    }

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupCreateGroup(navController = navController) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_signup),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
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
                        Text(text = "Name")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = {
                        Text(text = "Password")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                TextField(
                    value = order.restaurant.name,
                    onValueChange = { },
                    label = {
                        Text(text = "Restaurant")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    enabled = false
                )
                TextField(
                    value = order.direction,
                    onValueChange = { },
                    label = {
                        Text(text = "Address")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    enabled = false
                )
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
                onClick = {
                    group.name = groupName
                    group.password = password
                    orderViewModel.createGroup(group)
                    groupViewModel.createGroup(group, orderViewModel.user)
                    navController.navigate(AppScreens.Cart_Screen.route)
                },
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

/*
@Preview
@Composable
fun CreateGroupPreview() {
    val navController = rememberNavController()
    val orderViewModel = OrderViewModel()
    val groupViewModel = GroupViewModel()
    CreateGroupScreen(navController = navController, orderViewModel = orderViewModel, groupViewModel = groupViewModel)
}
 */