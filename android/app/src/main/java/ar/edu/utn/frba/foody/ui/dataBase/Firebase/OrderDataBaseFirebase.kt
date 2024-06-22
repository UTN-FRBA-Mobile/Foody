package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDataBaseFirebase(private var database: FirebaseDatabase) {
    private val TABLE_ORDERS = "orders"
    private val TABLE_USER_ORDERS = "userOrders"

    fun addOrder(order: Order): String {
        val myRef = database.getReference(TABLE_ORDERS)

        val key = myRef.push().key!!

        order.orderId = key.toString()

        myRef.child(order.orderId).setValue(order)

        return key;
    }

    fun addUserOrderToOrder(orderId: String, userOrder: UserOrder, callback: (Boolean) -> Unit) {
        val orderRef = FirebaseDatabase.getInstance().getReference(TABLE_ORDERS).child(orderId).child(TABLE_USER_ORDERS)

        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userOrdersList = mutableListOf<UserOrder>()
                dataSnapshot.children.forEach { snapshot ->
                    val existingUserOrder = snapshot.getValue(UserOrder::class.java)
                    existingUserOrder?.let {
                        userOrdersList.add(it)
                    }
                }
                userOrdersList.add(userOrder)
                orderRef.setValue(userOrdersList).addOnCompleteListener { task ->
                    callback(task.isSuccessful)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    fun updateUserOrder(orderId: String, userOrder: UserOrder, callback: (Boolean) -> Unit) {
        val orderRef = FirebaseDatabase.getInstance().getReference(TABLE_ORDERS).child(orderId).child(TABLE_USER_ORDERS)

        orderRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val userOrdersList = mutableListOf<UserOrder>()
                dataSnapshot.children.forEach { snapshot ->
                    val existingUserOrder = snapshot.getValue(UserOrder::class.java)
                    existingUserOrder?.let {
                        userOrdersList.add(it)
                    }
                }
                val index = userOrdersList.indexOfFirst { it.user.userId == userOrder.user.userId }
                if(index != -1) {
                    userOrdersList[index] = userOrder
                    orderRef.setValue(userOrdersList).addOnCompleteListener { task ->
                        callback(task.isSuccessful)
                    }
                } else {
                    callback(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(false)
            }
        })
    }

    fun getOrderById(orderId: String, callback: (Order?) -> Unit) {
        val myRef = database.getReference(TABLE_ORDERS)

        myRef.child(orderId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val order = dataSnapshot.getValue(Order::class.java)
                order?.orderId = dataSnapshot.key.toString()
                callback(order!!)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }


    fun getOrdersByUser(userId: String, callback: (List<Order>) -> Unit) {
        val myRef = database.getReference(TABLE_ORDERS)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                dataSnapshot.children.forEach { orderSnapshot ->
                    val order = orderSnapshot.getValue(Order::class.java)
                    order?.orderId = orderSnapshot.key.toString()
                    order?.userOrders?.forEach { userOrder ->
                        if(userOrder.user.userId == userId) {
                            orders.add(order)
                        }
                    }
                }
                callback(orders)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(emptyList())
            }
        })
    }
}