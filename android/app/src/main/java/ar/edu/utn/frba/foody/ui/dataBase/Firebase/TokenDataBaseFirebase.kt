package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.UserDeviceToken
import com.google.firebase.database.FirebaseDatabase

class TokenDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE = "user_device_tokens"

    fun addUserDeviceToken(token: String, userId: String) {
        val myRef = database.getReference(TABLE)

        val user = UserDeviceToken(token = token, userId = userId)

        myRef.child(user.userId).setValue(user)
    }
}