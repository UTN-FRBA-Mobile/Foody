package ar.edu.utn.frba.foody.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun ProfileScreen(
    navController: NavController, viewModel: MainViewModel,
    orderViewModel: OrderViewModel
) {
    val user = orderViewModel.user
    var email by remember { mutableStateOf(user.email) }
    var password by remember { mutableStateOf(user.password) }
    var numero by remember { mutableStateOf(user.contactNumber.toString()) }
    val direccion by remember { mutableStateOf(user.address) }
    val context = LocalContext.current

    AppScaffold(
        null,
        { TopGroupProfile(navController) }
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_signup),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 35.dp, vertical = 100.dp)
        ) {
            item {
                Text(
                    text = "Modificar Tu Información",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                )

                TextField(
                    value = email, onValueChange = { email = it },
                    label = { Text(text = "Nombre de Usuario", modifier = Modifier.padding(start = 16.dp)) },
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
                    label = { Text(text = "Contraseña", modifier = Modifier.padding(start = 16.dp)) },
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
                            text = "Numero de Contacto",
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
                    TextField(
                        value = "${direccion.street} ${direccion.number}, ${direccion.location}, ${direccion.country}",
                        onValueChange = {},
                        label = {
                            Text(
                                text = "Dirección",
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        enabled = false
                    )

                    IconButton(onClick = {
                        navController.navigate(
                            AppScreens.Location_Screen.createRoute(
                                "profile",
                                user.userId
                            )
                        )
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
                        if (validateAnyUserEmptyProf(user, user.address, context)) {
                            user.contactNumber = numero.toInt()

                            viewModel.updateUser(user)
                            navController.navigate(AppScreens.Home_Screen.route)
                        } else {
                            navController.navigate(AppScreens.Profile_Screen.route)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Editar", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun TopGroupProfile(navController: NavController) {
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

fun validateAnyUserEmptyProf(
    user: User,
    direccion: Address.AddressInfo,
    context: Context
): Boolean {
    if (direccion.street == "" || direccion.country == "" || direccion.location == ""
        || direccion.number == 0 || direccion.latitude == 0.0 || direccion.longitude == 0.0
    ) {
        Toast.makeText(context, "Dirección Invalida.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.email == "") {
        Toast.makeText(context, "Falta completar el email.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.password == "") {
        Toast.makeText(context, "Falta completar la contraseña.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.contactNumber == 0) {
        Toast.makeText(context, "Falta completar el numero de contacto.", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}