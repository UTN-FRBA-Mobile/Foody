package ar.edu.utn.frba.foody.ui.main

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ar.edu.utn.frba.foody.ui.theme.FoodyTheme

@Composable
fun AppScaffold(
    bottomAppBar: (@Composable () -> Unit)? = null,
    topAppBar: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit) {
    FoodyTheme {
        Scaffold(
            topBar = {
                     topAppBar?.invoke()
            },
            bottomBar = {
                BottomAppBar {
                    bottomAppBar?.invoke()
                }
            },
            content = {
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
    AppScaffold( content = {})
}