package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import android.database.Cursor
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_GROUPS = "groups"
    private val TABLE_USERS = "users"


    fun insertGroup(group: Group){
        val myRefGroups = database.getReference(TABLE_GROUPS)
        val key = myRefGroups.push().key!!

        group.groupId =  key.toString()
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

    fun getGroups(): List<Group>{
        val myRef = database.getReference(TABLE_GROUPS)

        val grouplist = mutableListOf<Group>()

        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children){
                    val group = dataSnapshot.getValue(Group::class.java)
                    if (group != null){
                        grouplist.add(group)
                    }
                }

            }
            override fun onCancelled(databaseError: DatabaseError) {
                grouplist.clear()
            }

        })

        return grouplist
    }

}