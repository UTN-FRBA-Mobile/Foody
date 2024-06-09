package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
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
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun SignUpScreen(navController: NavController)
{
    var Nombre by remember { mutableStateOf("") }
    var Email by remember { mutableStateOf("") }
    var Pass by remember { mutableStateOf("") }
    var Direc by remember { mutableStateOf("") }
    var Numero by remember { mutableStateOf("") }

    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize())


    IconButton(onClick = { navController.navigate(AppScreens.SignUp_Screen.route) }) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.go_back),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }
    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 35.dp, vertical = 100.dp)
    ){
        item {

            Text(
                text = "Create an Account",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )

            TextField(
                value = Nombre, onValueChange = { Nombre = it },
                label = { Text(text = "Nombre", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            TextField(
                value = Email, onValueChange = { Email = it },
                label = { Text(text = "Email", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            TextField(
                value = Pass, onValueChange = { Pass = it },
                label = { Text(text = "Password", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation(),
            )

            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ){
                TextField(
                    value = Direc, onValueChange = { Direc = it },
                    label = { Text(text = "Direccion", modifier = Modifier.padding(start = 16.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )
                IconButton(onClick = {
                    navController.navigate(AppScreens.Location_Screen.route)
                }) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.address_add_location),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            TextField(
                value = Numero, onValueChange = { Numero = it },
                label = { Text(text = "Numero Contacto", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {navController.navigate(AppScreens.Home_Screen.route)},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Registrar", fontSize = 18.sp)
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Do you have an account?", style = MaterialTheme.typography.body2)
                Spacer(modifier = Modifier.width(4.dp))
                ClickableText(
                    text = AnnotatedString("Sign in"),
                    onClick = { navController.navigate(AppScreens.Login_Screen.route) },
                    style = MaterialTheme.typography.body2.copy(color = MaterialTheme.colors.primary)
                )
            }


        }
    }
}


@Preview
@Composable
fun DefaultPreviewSignUp(){
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}