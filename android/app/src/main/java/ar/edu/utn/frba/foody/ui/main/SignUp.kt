package ar.edu.utn.frba.foody.ui.main

import android.content.Context
import android.widget.Toast
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
import androidx.compose.material.RadioButton
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
import androidx.compose.ui.semantics.Role.Companion.RadioButton
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.Firebase.UserDataBaseFirebase
import ar.edu.utn.frba.foody.ui.dataBase.SQLite.UserDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun SignUpScreen(
    navController: NavController,
    viewModel: OrderViewModel,
    mainViewModel: MainViewModel,
    dbUserDataBase: UserDataBase?,
    dbUserDataBaseFirebase: UserDataBaseFirebase
) {
    val user = User()
    val context = LocalContext.current

    val email = mainViewModel.email.value
    val password = mainViewModel.password.value
    val contactNumber = mainViewModel.contactNumber.value
    val delivery = mainViewModel.delivery.value

    AppScaffold(
        navController,
        null,
        null,
        { TopGroupSignUp() }
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
                .padding(
                    horizontal = 35.dp,
                    vertical = 50.dp
                )
        ) {
            item {
                Text(
                    text = "Crear una cuenta",
                    style = MaterialTheme.typography.h5,
                    color = MaterialTheme.colors.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp)
                )

                TextField(
                    value = email,
                    { mainViewModel.updateEmail(it) },
                    label = { Text(text = "Usuario", modifier = Modifier.padding(start = 16.dp)) },
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
                    value = password,
                    { mainViewModel.updatePassword(it) },
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
                    value = contactNumber,
                    onValueChange = { mainViewModel.updateContactNumber(it) },
                    label = {
                        Text(
                            text = "Número de contacto",
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
                    val direccionText = if (viewModel.user.direccion.calle != null && viewModel.user.direccion.calle != "")
                        viewModel.user.direccion.let { "${it.calle} ${it.numero}, ${it.localidad}, ${it.region}" }
                    else ""

                    TextField(
                        value = direccionText,
                        // Mostrar la dirección si está disponible, de lo contrario, vacío
                        onValueChange = {},  // No permitir cambios en el texto
                        label = {
                            Text(
                                text = "Dirección",
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                        enabled = false  // Deshabilitar la edición del TextField
                    )

                    IconButton(onClick = {
                        navController.navigate(
                            AppScreens.Location_Screen.createRoute(
                                "sign_up",
                                "0"
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

                Text(text = "Repartidor:", fontSize = 24.sp, modifier = Modifier.padding(bottom = 16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                     RadioButton(
                            selected = delivery == "Si",
                            onClick = { mainViewModel.updateDelivery("Si") }
                        )
                        Text(text = "Sí", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                        RadioButton(
                            selected = delivery == "No",
                            onClick = { mainViewModel.updateDelivery("No") }
                        )
                        Text(text = "No", fontSize = 18.sp, modifier = Modifier.padding(horizontal = 8.dp))
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Seleccionaste $delivery", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        user.email = email
                        user.password = password
                        if (validateAnyUserEmpty(user, contactNumber, context)) {
                            user.numeroContacto = contactNumber.toInt()

                            val address = viewModel.user.direccion

                            dbUserDataBaseFirebase.addUser(
                                user.email,
                                user.password,
                                address,
                                user.numeroContacto,
                                delivery
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
}

@Composable
fun TopGroupSignUp() {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        }
    )
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
    if (user.password.length < 8) {
        Toast.makeText(context, "La contraseña debe tener al menos 8 caracteres.", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}

/*@Preview
@Composable
fun DefaultPreviewSignUp() {
    val navController = rememberNavController()
    val addressViewModel = AddressViewModel()
    val database = FirebaseDatabase.getInstance()
    val context = LocalContext.current
    val dbUserHelper = UserDataBase(context)
    val userDataBaseFirebase = UserDataBaseFirebase(database)

    SignUpScreen(
        navController = navController,
        addressViewModel,
        dbUserHelper,
        userDataBaseFirebase
    )
}*/
