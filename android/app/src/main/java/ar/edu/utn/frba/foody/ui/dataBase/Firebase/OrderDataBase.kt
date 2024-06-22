package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OrderDataBase(private var database: FirebaseDatabase) {
    private val TABLE_ORDERS = "orders"

    fun addOrder(order: Order): Order {
        val myRef = database.getReference(TABLE_ORDERS)

        val key = myRef.push().key

        order.orderId = key.toString()

        myRef.child(order.orderId).setValue(order)

        return order;
    }

    fun addUserOrder(order: Order, userOrder: UserOrder): UserOrder {
        val myRef = database.getReference(TABLE_ORDERS)

        val key = myRef.push().key

        userOrder.userOrderId = key.toString()

        myRef.child(order.orderId).child("userOrders").child(userOrder.userOrderId).setValue(userOrder)

        return userOrder;
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