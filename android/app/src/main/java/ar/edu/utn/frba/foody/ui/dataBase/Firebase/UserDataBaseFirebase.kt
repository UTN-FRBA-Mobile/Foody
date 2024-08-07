package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_USER = "users"

    fun addUser(email: String, password: String, address: Address.AddressInfo,
                contactNumber: Int,delivery: String): User {
        val myRef = database.getReference(TABLE_USER)
        val user = User(email = email, password = password, address = address,
            contactNumber = contactNumber,delivery =delivery )
        user.userId = email.replace(".", "")
        myRef.child(user.userId).setValue(user)
        return user;
    }
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
    fun getAllUsers(callback: (List<User>) -> Unit) {
        val myRef = database.getReference(TABLE_USER)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val users = mutableListOf<User>()
                dataSnapshot.children.forEach { userSnapshot ->
                    val user = userSnapshot.getValue(User::class.java)
                    if (user != null) {
                        users.add(user)
                    }
                }
                if (users.isNotEmpty()) callback(users)
                else callback(emptyList())
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}