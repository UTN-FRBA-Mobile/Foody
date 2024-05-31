package ar.edu.utn.frba.foody.ui.Classes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import java.time.LocalDate

class Card {
    data class CardInfo(
        val cardNumber: String="",
        val firstName: String="",
        val lastName: String="",
        val expiryDate: String="",
        val cvv: String=""
    )
}