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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.navigation.AppScreens
import ar.edu.utn.frba.foody.ui.Classes.*
import ar.edu.utn.frba.foody.ui.dataClasses.CardViewModel
import java.util.Calendar


@Composable
fun CardInfoScreen(navController: NavHostController,viewModel: CardViewModel) {
    AppScaffold(navController, null,null,
        { TopGroupCard(navController)}){
        var cardInfo by remember { mutableStateOf(Card.CardInfo()) }

        val context = LocalContext.current

        Column(
        modifier = Modifier
            .fillMaxSize()
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
                onValueChange = { cardInfo =cardInfo.copy(firstName =  it) },
                label = { Text("Name") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                )
            )
            TextField(
                value = cardInfo.lastName,
                onValueChange = { cardInfo =cardInfo.copy(lastName =  it) },
                label = { Text("Last Name") },
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
                onValueChange = { cardInfo =cardInfo.copy(expiryDate =  it) },
                label = { Text("Expiry date(MM/YY)") },
                visualTransformation = ExpiryDateVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1.1f),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color.Transparent
                ),
                maxLines = 1
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
                if(validator(cardInfo,context)==true){
                    viewModel.addCard(cardInfo)
                    navController.popBackStack() // Replace with your next screen navigation
                }

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(25)
        ) {
            Text("Save", fontSize = 18.sp)
        }
    }
    }
}

@Composable
fun TopGroupCard(navController: NavController) {
    val button_go_back =
        ButtonInterface(
            resourceId = R.drawable.go_back,
            imageDescription = "Go Back Icon",
            route = AppScreens.Home_Screen.route,

            )
    Row(modifier = Modifier
        .fillMaxWidth()
        .background(MaterialTheme.colors.primarySurface),
        horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(
            onClick = { navController.popBackStack() },
        ) {
            Image(
                painter = painterResource(id = button_go_back.resourceId),
                contentDescription = button_go_back.imageDescription,
                modifier = Modifier.size(24.dp),
                contentScale = ContentScale.FillBounds
            )
        }
    }
}

fun validator(cardInfo: Card.CardInfo,context: Context):Boolean {
    if (validateExpiryDate(cardInfo.expiryDate,context)=="" || validateCVV(cardInfo.cvv,context) =="" ||
        cardInfo.cardNumber=="" || cardInfo.lastName=="" || cardInfo.firstName=="")
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
        Toast.makeText(context, "Invalid month. Must be between 01 and 12.", Toast.LENGTH_SHORT).show()
        return ""
    }

    if (year == null || year < currentYear) {
        Toast.makeText(context, "Invalid year. Must be the current year or later.", Toast.LENGTH_SHORT).show()
        return ""
    }

    return cleanedInput
}

fun validateCVV(input: String,context:Context): String {
    val cleanedInput = input.filter { it.isDigit() }
    if (cleanedInput.length <3 ) {
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

@Preview
@Composable
fun DefaultPreviewCardInfo() {
    val navController= rememberNavController()
    val cardViewModel= CardViewModel()
   CardInfoScreen(navController,cardViewModel)
}