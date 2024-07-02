package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_USER = "users"

    fun addUser(email: String, password: String, direccion: Address.AddressInfo,
                numeroContacto: Int,repartidor: String): User {
        val myRef = database.getReference(TABLE_USER)

        val user = User(email = email, password = password, direccion = direccion,
            numeroContacto = numeroContacto,repartidor =repartidor )

        user.userId = email.replace(".", "")

        myRef.child(user.userId).setValue(user)

        return user;
    }

    //Create method to update user
    fun updateUser(user: User) {
        val myRef = database.getReference(TABLE_USER)

        myRef.child(user.userId).setValue(user)
    }

    fun getUserByEmail(email: String, callback: (User?) -> Unit) {
        val myRef = database.getReference(TABLE_USER)

        val userId = email.replace(".", "")

        val ref = myRef.child(userId)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.userId = userId
                    callback(user)
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


