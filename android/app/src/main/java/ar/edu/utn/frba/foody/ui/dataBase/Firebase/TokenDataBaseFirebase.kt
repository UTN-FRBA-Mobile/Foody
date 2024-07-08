package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.UserDeviceToken
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TokenDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE = "user_device_tokens"

    fun addUserDeviceToken(token: String, userId: String) {
        val myRef = database.getReference(TABLE)

        val user = UserDeviceToken(token = token, userId = userId)

        myRef.child(user.token).setValue(user)
    }
    
    fun deleteUserDeviceToken(token: String, userId: String) {
        val myRef = database.getReference(TABLE)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { snapshot ->
                    val selectedToken = snapshot.child("token").getValue(String::class.java)
                    val selectedUserId = snapshot.child("userId").getValue(String::class.java)

                    if(token == selectedToken && userId == selectedUserId) {
                        snapshot.ref.removeValue()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
            }
        })
    }
}