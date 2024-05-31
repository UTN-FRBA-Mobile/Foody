package ar.edu.utn.frba.foody.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ar.edu.utn.frba.foody.R


@Composable
fun SimpleTextField(labelValue: String){
    var value by remember { mutableStateOf("") }

    TextField(
        value = value,
        onValueChange = { value = it },
        label = { Text(text = labelValue, modifier = Modifier.padding(start = 16.dp)) },
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

}

@Composable
fun PaswordTextField(labelValue: String){
    var value by remember { mutableStateOf("") }

    TextField(
        value = value, onValueChange = { value = it },
        label = { Text(text = labelValue, modifier = Modifier.padding(start = 16.dp)) },
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

}

@Preview
@Composable
fun DefaultPreviewTextFieldComponents(){
    Column {
        SimpleTextField(labelValue = "Email")
        PaswordTextField(labelValue = "Password")
    }
}