package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.ui.UiString

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onButtonClicked: (UiString) -> Unit,
    navController: NavController? = null) {
    AppScaffold(navController = navController) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Correo electrónico",
                modifier = Modifier
                    .padding(start = 16.dp, top = 16.dp)
            )

        }
    }
}

@Preview
@Composable
fun DefaultPreview() {
    val viewModel = viewModel<MainViewModel>()
    viewModel.labelBotonIngresar = "boton"
    HomeScreen(
        viewModel = viewModel,
        onButtonClicked = {}
    )
}
