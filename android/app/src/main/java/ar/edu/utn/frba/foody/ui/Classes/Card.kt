package ar.edu.utn.frba.foody.ui.Classes

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
@SuppressLint("MutableCollectionMutableState")
class CardViewModel : ViewModel() {
    var cards by mutableStateOf(mutableStateListOf<Card.CardInfo>())
        private set

    fun addCard(cardInfo: Card.CardInfo) {
        cards.add(cardInfo)
    }
}