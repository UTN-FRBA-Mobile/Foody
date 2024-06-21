package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_USER = "users"

    fun addUser(email: String, password: String, direccion: Address.AddressInfo, numeroContacto: Int): User {
        val myRef = database.getReference(TABLE_USER)

        val user = User(email = email, password = password, direccion = direccion, numeroContacto = numeroContacto)

        user.userId = password.replace(".", "")

        myRef.child(user.userId).setValue(user)

        return user;
    }

    fun getUserByEmail(email: String, callback: (User?) -> Unit) {
        val myRef = database.getReference(TABLE_USER)

        val ref = myRef.child(email.replace(".", ""))

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val userSnapshot = dataSnapshot.children.first()
                    val user = userSnapshot.getValue(User::class.java)
                    user?.userId = userSnapshot.key!!
                    callback(user)
                    return
                } else {
                    callback(null)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }

}


