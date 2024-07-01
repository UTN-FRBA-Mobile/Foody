package ar.edu.utn.frba.foody.ui.main

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import ar.edu.utn.frba.foody.ui.Classes.*
import ar.edu.utn.frba.foody.ui.dataClasses.MainViewModel
import ar.edu.utn.frba.foody.ui.dataClasses.OrderViewModel
import java.util.Calendar


@Composable
fun CardInfoScreen(navController: NavHostController, mainViewModel: MainViewModel,
                   orderViewModel: OrderViewModel
) {
    AppScaffold(navController,
        null,
        null,
        { TopGroupCard(navController)}
    ){
        var cardInfo by remember { mutableStateOf(Card.CardInfo()) }
        val user= orderViewModel.user

        val context = LocalContext.current

        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.background_signup),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Text(
                    text = "Add new card",
                    style = MaterialTheme.typography.h5.copy(fontSize = 24.sp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    textAlign = TextAlign.Left
                )
                // Card number input
                TextField(
                    value = cardInfo.cardNumber,
                    onValueChange = { cardInfo = cardInfo.copy(cardNumber = it) },
                    label = { Text("Card number") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    maxLines = 1,
                    trailingIcon = {
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Name and Last Name inputs
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = cardInfo.firstName,
                        onValueChange = { cardInfo = cardInfo.copy(firstName = it) },
                        label = { Text("Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        )
                    )
                    TextField(
                        value = cardInfo.lastName,
                        onValueChange = { cardInfo = cardInfo.copy(lastName = it) },
                        label = { Text("Last Name") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Expiry date and CVV inputs
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextField(
                        value = cardInfo.expiryDate,
                        onValueChange = { cardInfo = cardInfo.copy(expiryDate = it) },
                        label = { Text("Expiry date(MM/YY)") },
                        visualTransformation = ExpiryDateVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.weight(1.1f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        ),
                    )


                    TextField(
                        value = cardInfo.cvv,
                        onValueChange = {
                            if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                cardInfo = cardInfo.copy(cvv = it)
                            }
                        },
                        label = { Text("CVC/CVV") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        maxLines = 1,
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.weight(1f),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.Transparent
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Disclaimer text

                Spacer(modifier = Modifier.height(8.dp))

        // Save button
        Button(
            onClick = {
                if (validator(cardInfo, context) == true){
                    //viewModel.addCard(cardInfo)
                    user.tarjetas.add(cardInfo)
                    mainViewModel.updateUser(user)
                    navController.popBackStack() // Replace with your next screen navigation
                }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Save", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun TopGroupCard(navController: NavController) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = {
            IconButton(onClick = { navController.navigate(AppScreens.Payment.route) }) {
                Image(
                    painter = painterResource(id = R.drawable.go_back),
                    contentDescription = "Go Back Icon",
                    modifier = Modifier.size(24.dp),
                    contentScale = ContentScale.FillBounds
                )
            }
        }
    )
}

fun validator(cardInfo: Card.CardInfo, context: Context): Boolean {
    if (validateExpiryDate(cardInfo.expiryDate, context) == "" || validateCVV(
            cardInfo.cvv,
            context
        ) == "" ||
        cardInfo.cardNumber == "" || cardInfo.lastName == "" || cardInfo.firstName == ""
    )
        return false
    return true
}

fun validateExpiryDate(input: String, context: Context): String {
    val cleanedInput = input.filter { it.isDigit() }
    if (cleanedInput.length > 4) return input.dropLast(1)

    val month = cleanedInput.take(2).toIntOrNull()
    val year = cleanedInput.takeLast(2).toIntOrNull()
    val currentYear = Calendar.getInstance().get(Calendar.YEAR) % 100

    if (month == null || (month < 1 || month > 12)) {
        Toast.makeText(context, "Invalid month. Must be between 01 and 12.", Toast.LENGTH_SHORT)
            .show()
        return ""
    }

    if (year == null || year < currentYear) {
        Toast.makeText(
            context,
            "Invalid year. Must be the current year or later.",
            Toast.LENGTH_SHORT
        ).show()
        return ""
    }

    return cleanedInput
}

fun validateCVV(input: String, context: Context): String {
    val cleanedInput = input.filter { it.isDigit() }
    if (cleanedInput.length < 3) {
        Toast.makeText(context, "Invalid CVV.", Toast.LENGTH_SHORT).show()
        return ""
    }
    return input
}

class ExpiryDateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val trimmed = if (text.text.length >= 4) text.text.substring(0, 4) else text.text
        val out = StringBuilder()
        for (i in trimmed.indices) {
            out.append(trimmed[i])
            if (i == 1) {
                out.append("/")
            }
        }
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset + 1
                return 5
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 2) return offset
                if (offset <= 5) return offset - 1
                return 4
            }
        }
        return TransformedText(AnnotatedString(out.toString()), offsetMapping)
    }
}

/*
@Preview
@Composable
fun DefaultPreviewCardInfo() {
    val navController= rememberNavController()
    val mainViewModel = MainViewModel()
    val orderViewModel = OrderViewModel()
   CardInfoScreen(navController,mainViewModel,orderViewModel)
}

 */