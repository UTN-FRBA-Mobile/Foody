package ar.edu.utn.frba.foody

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ar.edu.utn.frba.foody.ui.UiString
import ar.edu.utn.frba.foody.ui.composables.SimpleAlert
import ar.edu.utn.frba.foody.ui.main.HomeScreen
import ar.edu.utn.frba.foody.ui.main.LoginScreen
import ar.edu.utn.frba.foody.ui.main.MainViewModel
import ar.edu.utn.frba.foody.ui.navigation.AppNavigation

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppNavigation()
        }
    }
}

