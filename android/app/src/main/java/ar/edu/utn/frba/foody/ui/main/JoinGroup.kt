package ar.edu.utn.frba.foody.ui.main

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.dataBase.GroupDataBase
import ar.edu.utn.frba.foody.ui.dataBase.OrderDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun JoinGroupScreen(
    navController: NavHostController,
    dbHelper: GroupDataBase,
    dbOrderHelper: OrderDataBase,
    orderViewModel: OrderViewModel
) {
    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    AppScaffold(navController = navController,
        null,
        null,
        { TopGroupJoinGroup(navController = navController) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Join to a Group",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (showError) {
                    Text(
                        text = "Incorrect Name or Password",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }


                Button(
                    onClick = {
                        val group = verifyGroupExist(dbHelper, name, password)
                        if (group != null) {
                            orderViewModel.removeOrderFromSession()
                            orderViewModel.updateGroup(group)
                            navController.navigate(AppScreens.Home_Screen.route)
                        } else {
                            showError = true
                            name = ""
                            password = ""
                            Toast.makeText(
                                navController.context,
                                "Incorrect Name or Password",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Join", fontSize = 18.sp)
                }

                Spacer(modifier = Modifier.height(100.dp))

            }
        }
    }
}

@Composable
fun TopGroupJoinGroup(navController: NavController) {
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

fun verifyGroupExist(dbHelper: GroupDataBase, name: String, password: String): Group? {
    val groups: List<Group> = dbHelper.getAllGroups(groupDataBase = dbHelper)
    val group: Group? = groups.find { groupPred: Group -> groupPred.name == name }
    if (group != null && group.password == password) {
        return group
    }
    return null
}