package ar.edu.utn.frba.foody.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.utn.frba.foody.R

@Composable
fun InputAlert(
    show: Boolean,
    text: String,
    validInputValue: String,
    onConfirm: () -> Unit,
    invalidInputCondition: MutableState<Boolean>,
    error: MutableState<Boolean>,
    onDismiss: () -> Unit
) {
    var inputValue by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var validIntents by remember { mutableStateOf(3) }

    if (show) {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Button(
                    onClick = {
                        if (inputValue != validInputValue) {
                            validIntents--

                            if (validIntents == 0) {
                                invalidInputCondition.value = true
                                onDismiss()
                                error.value = true
                            } else {
                                showError = true
                            }
                        } else {
                            onConfirm()
                        }
                    },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                {
                    Text(text = stringResource(id = R.string.aceptar))
                }
                TextButton(
                    onClick = { onDismiss() },
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                {
                    Text(text = stringResource(id = R.string.cancelar))
                }
            },
            text = {
                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body1,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Espacio entre el título y el TextField
                    TextField(
                        value = inputValue,
                        onValueChange = { inputValue = it },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        )
                    )
                    if (showError) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "El valor ingresado es incorrecto. Intentos restantes: $validIntents",
                            color = MaterialTheme.colors.error,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun DefaultPreview() {
    InputAlert(
        show = true,
        text = "Por favor ingrese el cvv para validar su tarjeta",
        validInputValue = "1234",
        onConfirm = {},
        invalidInputCondition = mutableStateOf(value = false),
        error = mutableStateOf(value = false),
        onDismiss = {}
    )
}