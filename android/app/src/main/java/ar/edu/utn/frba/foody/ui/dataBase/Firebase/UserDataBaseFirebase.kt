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

        // Replace '.' with '' in the email to use it as a Firebase key
        val userId = email.replace(".", "")

        // Reference to the specific user node
        val ref = myRef.child(userId)

        // Attach a single value event listener to read data once
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Check if the snapshot exists (i.e., the user exists in the database)
                if (dataSnapshot.exists()) {
                    // Get the User object from the snapshot
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.userId = dataSnapshot.key!! // Assign the key (userId) to the User object
                    callback(user) // Return the user via the callback
                } else {
                    callback(null) // Return null if the user does not exist
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null) // Return null if there is a database error
            }
        })
    }



}


