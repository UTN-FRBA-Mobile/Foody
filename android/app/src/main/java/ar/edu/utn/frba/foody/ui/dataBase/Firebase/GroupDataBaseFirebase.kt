package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class GroupDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_GROUPS = "groups"
    private val TABLE_USERS = "users"

    fun insertGroup(group: Group) {
        val myRefGroups = database.getReference(TABLE_GROUPS)

        myRefGroups.child(group.groupId).setValue(group)

        group.members.forEach { user: User ->
            val myRefUsers = database.getReference(TABLE_USERS)
            user.groupId = group.groupId
            myRefUsers.child(user.userId).setValue(user)
        }

    }
    fun addUser(group: Group, user: User) {
        val myRef = database.getReference(TABLE_GROUPS)
        myRef.child(group.groupId).setValue(group)

        val myRefUsers = database.getReference(TABLE_USERS)
        user.groupId = group.groupId
        myRefUsers.child(user.userId).setValue(user)
    }
    fun removeUser(groupId: String, user: User, callback: (Boolean) -> Unit) {
        val membersRef = database.getReference(TABLE_GROUPS).child(groupId).child("members")

        if (user.admin) {
            reassignAdmin(groupId) { userReassigned ->
                if (userReassigned != null) {
                    changeUserRole(userReassigned, true)
                    changeUserRole(user, false)
                } else {
                    changeUserRole(user, false)
                }
            }
        }
        membersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var userFound = false

                for (memberSnapshot in snapshot.children) {
                    val memberUserId = memberSnapshot.child("userId").getValue(String::class.java)

                    if (memberUserId == user.userId) {
                        memberSnapshot.ref.removeValue().addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val usersRef = database.getReference(TABLE_USERS).child(user.userId)
                                usersRef.child("groupId").setValue("")

                                membersRef.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (!snapshot.exists()) {
                                            val groupRef = database.getReference(TABLE_GROUPS).child(groupId)
                                            groupRef.removeValue().addOnCompleteListener { groupTask ->
                                                if (groupTask.isSuccessful) {
                                                    callback(true)
                                                } else {
                                                    callback(false)
                                                }
                                            }
                                        } else {
                                            callback(true)
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        callback(false)
                                    }
                                })
                            } else {
                                callback(false)
                            }
                        }
                        userFound = true
                        break
                    }
                }
                if (!userFound) {
                    callback(false)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(false)
            }
        })
    }
    private fun changeUserRole(userReassigned: User, adminRole: Boolean) {
        database.getReference(TABLE_USERS).child(userReassigned.userId).child("admin")
            .setValue(adminRole)
    }
    private fun reassignAdmin(groupId: String, callback: (User?) -> Unit) {
        val groupRef = database.getReference(TABLE_GROUPS).child(groupId).child("members")
        groupRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var firstNonAdminFound = false

                snapshot.children.forEach { memberSnapshot ->
                    val isAdmin = memberSnapshot.child("admin").getValue(Boolean::class.java)

                    if (isAdmin != true && !firstNonAdminFound) {
                        memberSnapshot.ref.child("admin").setValue(true)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    firstNonAdminFound = true
                                    val user = memberSnapshot.getValue(User::class.java)
                                    callback(user)
                                } else {
                                    callback(null)
                                }
                            }
                    } else {
                        callback(null)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                callback(null)
            }
        })
    }
    fun getGroupByName(name: String, callback: (Group?) -> Unit) {
        val myRef = database.getReference(TABLE_GROUPS)
        val ref = myRef.child(name)

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val group = dataSnapshot.getValue(Group::class.java)

                if (group != null) {
                    group.groupId = name
                    callback(group)
                } else {
                    callback(null)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
    fun getGroupByUserId(user:User, callback: (Group?) -> Unit) {
        val myRef = database.getReference(TABLE_GROUPS)
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot.children.forEach { groupSnapshot ->
                    val group = groupSnapshot.getValue(Group::class.java)
                    if (group != null && group.members.any{member->member.userId==user.userId}) {
                        callback(group)
                        return
                    }
                }
                callback(null)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
}