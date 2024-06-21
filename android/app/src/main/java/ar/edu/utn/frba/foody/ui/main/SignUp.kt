package ar.edu.utn.frba.foody.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.AddressViewModel
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.UserDataBase
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: AddressViewModel,
    dbUserDataBase: UserDataBase?
) {
    val user = User()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var numero by remember { mutableStateOf("") }

    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )


    IconButton(onClick = { navController.navigate(AppScreens.Login_Screen.route) }) {
        Icon(
            modifier = Modifier.size(36.dp),
            painter = painterResource(id = R.drawable.go_back),
            contentDescription = null,
            tint = MaterialTheme.colors.primary
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 35.dp, vertical = 100.dp)
    ) {
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
                value = email, onValueChange = { email = it },
                label = { Text(text = "Email", modifier = Modifier.padding(start = 16.dp)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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
                value = password, onValueChange = { password = it },
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

            TextField(
                value = numero, onValueChange = { numero = it },
                label = {
                    Text(
                        text = "Numero Contacto",
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                val direccionText = if (viewModel.existAddress()) viewModel.getPickedAddress()
                    .let { "${it.calle} ${it.numero}, ${it.localidad}, ${it.region}" } else ""

                TextField(
                    value = direccionText,
                    // Mostrar la dirección si está disponible, de lo contrario, vacío
                    onValueChange = {},  // No permitir cambios en el texto
                    label = {
                        Text(
                            text = "Direccion",
                            modifier = Modifier.padding(start = 16.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    enabled = false  // Deshabilitar la edición del TextField
                )

                IconButton(onClick = {
                    navController.navigate(AppScreens.Location_Screen.createRoute("sign_up", "0"))
                }) {
                    Icon(
                        modifier = Modifier.size(36.dp),
                        painter = painterResource(id = R.drawable.address_add_location),
                        contentDescription = null,
                        tint = MaterialTheme.colors.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = {
                    user.email = email
                    user.password = password
                    if (validateAnyUserEmpty(user, numero, context)) {
                        user.numeroContacto = numero.toInt()
                        val addressId =
                            dbUserDataBase?.addAddress(dbUserDataBase, viewModel.getPickedAddress())

                        dbUserDataBase?.addUser(
                            dbUserDataBase,
                            user.email,
                            user.password,
                            addressId,
                            user.numeroContacto
                        )
                        viewModel.emptyAddress()

                        navController.navigate(AppScreens.Login_Screen.route)
                    } else {
                        navController.navigate(AppScreens.SignUp_Screen.route)
                    }


                },
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

fun validateAnyUserEmpty(
    user: User,
    numero: String,
    context: Context
): Boolean {
    if (user.email == "") {
        Toast.makeText(context, "Falta completar el email.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.password == "") {
        Toast.makeText(context, "Falta completar la contraseña.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (numero == "") {
        Toast.makeText(context, "Falta completar el numero de contacto.", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}


@Preview
@Composable
fun DefaultPreviewSignUp() {
    val navController = rememberNavController()
    val addressViewModel = AddressViewModel()

    SignUpScreen(navController = navController, addressViewModel, null)
}