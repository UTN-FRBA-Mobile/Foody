package ar.edu.utn.frba.foody.ui.main

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun LoginScreen(
    navController: NavHostController,
    mainViewModel: MainViewModel,
    orderViewModel: OrderViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val showError by remember { mutableStateOf(false) }
    val canGoBack = remember { mutableStateOf(false) }
    BackHandler(enabled = !canGoBack.value) {
    }
    AppScaffold(
        null,
        { TopGroupLogin() }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 56.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_login),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        bottom = 70.dp
                    )
                    .fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foody_logo),
                    contentDescription = "Delivery Logo",
                    modifier = Modifier
                        .size(120.dp)
                        .padding(bottom = 16.dp)
                )


                Text(
                    text = "Bienvenido a Foody",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Nombre de Usuario") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    singleLine = true,
                    maxLines = 1,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    maxLines = 1,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (showError) {
                    Text(
                        text = "Nombre de Usuario o Contraseña Incorrecta",
                        color = MaterialTheme.colors.error,
                        style = MaterialTheme.typography.body2
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }


                Button(
                    onClick = {
                        mainViewModel.fetchUserByEmail(email, password)
                        email = ""
                        password = ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Login", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("No tienes una cuenta?", style = MaterialTheme.typography.body2)
                    Spacer(modifier = Modifier.width(4.dp))
                    ClickableText(
                        text = AnnotatedString("Registrarte"),
                        onClick = {
                            orderViewModel.emptyAddress()
                            navController.navigate(AppScreens.SignUp_Screen.route)
                            mainViewModel.clearSignUpFields()
                        },
                        style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary)
                    )
                }
                Spacer(modifier = Modifier.height(100.dp))

            }
        }
    }
}

@Composable
fun TopGroupLogin() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        }
    )
}