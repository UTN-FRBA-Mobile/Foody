package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import ar.edu.utn.frba.foody.ui.theme.FoodyTheme

@Composable
fun AppScaffold(
    navController: NavController,
    title: String? = null,
    content: @Composable (PaddingValues) -> Unit) {
    FoodyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Button(
                            onClick = { navController.navigate(AppScreens.Login_Screen.route) },
                            colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color.Transparent,
                            ),
                            elevation = ButtonDefaults.elevation(0.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.logout_icon),
                                contentDescription = "Logout Icon",
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                        Text(text = title ?: stringResource(id = R.string.app_name))
                    }
                )
            },
            content = {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    content(it)
                }
            }
        )
    }
}

@Preview
@Composable
fun DefaultsPreview() {
    val navController= rememberNavController()
    AppScaffold(navController = navController, content = {})
}