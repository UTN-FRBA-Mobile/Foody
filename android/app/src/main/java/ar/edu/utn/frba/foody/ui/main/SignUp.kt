package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.components.PaswordTextField
import ar.edu.utn.frba.foody.ui.components.SimpleTextField

@Composable
fun SignUpScreen(navController: NavController)
{
    Image(
        painter = painterResource(id = R.drawable.background_login),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize())

    LazyColumn (modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 35.dp, vertical = 100.dp)){
        item {

            Text(
                text = "Create an Account",
                style = MaterialTheme.typography.h5,
                color = MaterialTheme.colors.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp)
            )

            SimpleTextField("Name")

            SimpleTextField("Email")

            PaswordTextField("Password")

            SimpleTextField("Address")

            SimpleTextField("Number")


        }
    }
}


@Preview
@Composable
fun DefaultPreviewSignUp(){
    val navController = rememberNavController()
    SignUpScreen(navController = navController)
}