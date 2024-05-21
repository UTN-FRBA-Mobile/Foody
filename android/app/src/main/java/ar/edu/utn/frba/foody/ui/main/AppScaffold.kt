package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.*
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
    bottomAppBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit) {
    FoodyTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(text = title ?: stringResource(id = R.string.app_name))
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate(AppScreens.Login_Screen.route) }) {
                            Image(
                                painter = painterResource(id = R.drawable.logout_icon),
                                contentDescription = "Logout Icon",
                                modifier = Modifier.size(24.dp),
                                contentScale = ContentScale.FillBounds
                            )
                        }
                    }
                )
            },
            bottomBar = {
                BottomAppBar {
                    bottomAppBar?.invoke()
                }
            },
            content = {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.secondary) {
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