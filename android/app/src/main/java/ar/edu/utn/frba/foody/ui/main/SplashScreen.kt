package ar.edu.utn.frba.foody.ui.main

import android.content.Intent
import android.window.SplashScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController : NavHostController,intent: Intent,orderViewModel: OrderViewModel){

    LaunchedEffect(key1 = true) {
        delay(1000)
        navController.popBackStack()
        navController.navigate(AppScreens.Session_Screen.route)
    }

    Splash()
}

@Composable
fun Splash(){
    Image(
        painter = painterResource(id = R.drawable.background_signup),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxSize()
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.foody_logo),
            contentDescription = "Logo foody",
            modifier = Modifier.size(150.dp, 150.dp))
        Text("Bienvenid@s",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview(){
    Splash()
}