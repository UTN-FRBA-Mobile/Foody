package ar.edu.utn.frba.foody.ui.composables

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import ar.edu.utn.frba.foody.R

@Composable
fun SimpleAlert(show: Boolean, text: String, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                TextButton(onClick = { onConfirm() })
                {
                    Text(text = stringResource(id = R.string.aceptar))
                }
            },
            title = { Text(text = text) })
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    SimpleAlert(
        show = true,
        text = "Hola",
        onDismiss = {},
        onConfirm = {}
    )
}