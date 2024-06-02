package ar.edu.utn.frba.foody.ui.dataClasses

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ar.edu.utn.frba.foody.R
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.*

@SuppressLint("MutableCollectionMutableState")
class CardViewModel() : ViewModel() {
    private var card by mutableStateOf(Card.CardInfo())
        private set

    fun updateCard(newCard: Card.CardInfo) {
        card = newCard
    }

    fun getPickedCard(): Card.CardInfo {
        return card
    }

    //TODO: pasar los datos estáticos a otro lado
    var cards: MutableList<Card.CardInfo> by mutableStateOf(
        mutableListOf(
            Card.CardInfo(cardNumber = "1234567812345678", firstName = "John",
                lastName = "Doe", expiryDate = "12/23", cvv = "123"),
            Card.CardInfo(cardNumber = "8765432187654321", firstName = "Jane",
                lastName = "Doe", expiryDate = "11/24", cvv = "321")
        )
    )
        private set

    fun addCard(cardNew:Card.CardInfo){
        cards.add(cardNew)
    }
    fun removeCard(cardRemove: Card.CardInfo){
        cards.remove(cardRemove)
    }
}


