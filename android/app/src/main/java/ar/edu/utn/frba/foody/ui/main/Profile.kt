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
import androidx.compose.ui.platform.LocalContext
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
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.dataClasses.AddressViewModel
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.dataBase.UserDataBase
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens

@Composable
fun ProfileScreen(navController: NavController, viewModel: AddressViewModel,
                  dbUserDataBase: UserDataBase?,orderViewModel: OrderViewModel)
{
    var user=orderViewModel.user
    var email= user.email
    var password= user.password
    var numero = user.numeroContacto
    var direccion = dbUserDataBase?.getAddress(user.direccion)
    val context = LocalContext.current

    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize())


    IconButton(onClick = { navController.navigate(AppScreens.Home_Screen.route) }) {
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
                text = "Edit Your Information",
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
                value = numero.toString(), onValueChange = { numero = it.toInt() },
                label = { Text(text = "Numero Contacto", modifier = Modifier.padding(start = 16.dp)) },
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
            Row (
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ){
                val direccionText = direccion?.let { "${it.calle} ${it.numero}, ${it.localidad}, ${it.region}" } ?: ""

                TextField(
                    value = direccionText,
                    onValueChange = {},
                    label = { Text(text = "Direccion", modifier = Modifier.padding(start = 16.dp)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    singleLine = true,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    enabled = false  // Deshabilitar la edición del TextField
                )
                IconButton(onClick = {
                    navController.navigate(AppScreens.Location_Screen.createRoute("profile"))
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
                    user.email=email
                    user.password=password
                    user.numeroContacto=numero.toInt()
                    if(validateAnyUserEmpty(user,viewModel.getPickedAddress(),context)) {
                        val addressId =
                            dbUserDataBase?.addAddress(dbUserDataBase, viewModel.getPickedAddress())//UPDATE ADDRESS
                        if (addressId != null) {
                            user.direccion=addressId
                        }
                        dbUserDataBase?.updateUser(
                            dbUserDataBase,
                           user
                        )
                        navController.navigate(AppScreens.Login_Screen.route)
                    }
                    else {navController.navigate(AppScreens.SignUp_Screen.route)}


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

fun validateAnyUserEmptyProf(user:User,direccion:Address.AddressInfo,context: Context):Boolean{
    if(direccion.calle=="" || direccion.region=="" || direccion.localidad==""
        || direccion.numero==0 || direccion.latitud==0.0 || direccion.longitud==0.0){
        Toast.makeText(context, "Dirección Invalida.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.email=="") {
        Toast.makeText(context, "Falta completar el email.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.password==""){
        Toast.makeText(context, "Falta completar la contraseña.", Toast.LENGTH_SHORT).show()
        return false
    }
    if (user.numeroContacto==0){
        Toast.makeText(context, "Falta completar el numero de contacto.", Toast.LENGTH_SHORT).show()
        return false
    }

    return true
}


@Preview
@Composable
fun DefaultPreviewProfile(){
    val navController = rememberNavController()
    val addressViewModel= AddressViewModel()

    SignUpScreen(navController = navController,addressViewModel,null)
}