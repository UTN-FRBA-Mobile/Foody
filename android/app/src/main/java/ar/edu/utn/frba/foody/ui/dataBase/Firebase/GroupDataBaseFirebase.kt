package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import android.database.Cursor
import androidx.compose.runtime.mutableStateOf
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await

class GroupDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_GROUPS = "groups"
    private val TABLE_USERS = "users"


    fun insertGroup(group: Group){
        val myRefGroups = database.getReference(TABLE_GROUPS)

        myRefGroups.child(group.groupId).setValue(group)

        group.members.forEach{user:User ->
            val myRefUsers = database.getReference(TABLE_USERS)
            user.groupId =  group.groupId
            myRefUsers.child(user.userId).setValue(user)
        }

    }

    fun updateGroup(group: Group){
        val myRef = database.getReference(TABLE_GROUPS)
        myRef.child(group.groupId).setValue(group)

        group.members.forEach{user:User ->
            val myRefUsers = database.getReference(TABLE_USERS)

            user.userId =  group.groupId
            myRefUsers.child(user.userId).setValue(user)
        }
    }

    fun getGroupByName(name : String) : Group?{
        val myRef = database.getReference(TABLE_GROUPS)

        var res : Group? = null

        val ref = myRef.child(name)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    val group = dataSnapshot.getValue(Group::class.java)
                    group?.groupId = name
                    res = group!!
                } else{
                    res = null
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                res = null
            }
        })

        return res
    }
    /*
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
     */
}