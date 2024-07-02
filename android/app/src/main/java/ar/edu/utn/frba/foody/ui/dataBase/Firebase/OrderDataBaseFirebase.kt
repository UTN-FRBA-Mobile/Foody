package ar.edu.utn.frba.foody.ui.dataBase.Firebase

import ar.edu.utn.frba.foody.ui.Classes.Estado
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.User
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

    fun updateUserOrderList(orderId: String, userOrders: List<UserOrder>, callback: (Boolean) -> Unit) {
        val orderRef = FirebaseDatabase.getInstance().getReference(TABLE_ORDERS).child(orderId).child(TABLE_USER_ORDERS)

        orderRef.setValue(userOrders).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    fun deleteOrder(orderId: String, callback: (Boolean) -> Unit) {
        val orderRef = FirebaseDatabase.getInstance().getReference(TABLE_ORDERS).child(orderId)

        orderRef.removeValue().addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }
    fun updateOrder(order: Order, callback: (Boolean) -> Unit){
        val orderRef= FirebaseDatabase.getInstance().getReference(TABLE_ORDERS).child(order.orderId)
        orderRef.setValue(order).addOnCompleteListener { task ->
            callback(task.isSuccessful)
        }
    }

    fun getOrderById(orderId: String, callback: (Order?) -> Unit) {
        val myRef = database.getReference(TABLE_ORDERS)

        myRef.child(orderId).addValueEventListener(object : ValueEventListener {
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
    fun getOrderByState(estado: Estado,user: User, callback: (Order?) -> Unit) {
        val myRef = database.getReference(TABLE_ORDERS)

        myRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    val order = snapshot.getValue(Order::class.java)
                    if (order != null && order.estado == estado) {
                        val userOrders = order.userOrders // Asumiendo que es una lista de objetos de tipo UserOrder
                        for (userOrder in userOrders) {
                            if (userOrder.user.userId == user.userId) {
                                order.orderId = snapshot.key.toString()
                                callback(order) // Retorna la orden filtrada
                                return // Termina la búsqueda después de encontrar la primera coincidencia
                            }
                        }
                    }
                }
                callback(null) // Si no se encuentra ninguna orden, retorna null
            }

            override fun onCancelled(databaseError: DatabaseError) {
                callback(null)
            }
        })
    }
    fun getOrdersByUser(userId: String, callback: (List<Order>) -> Unit) {
        val myRef = database.getReference(TABLE_ORDERS)

        myRef.addValueEventListener(object : ValueEventListener {
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
    fun getOrdersByState(callback: (List<Order>) -> Unit) {
        val myRef = database.getReference(TABLE_ORDERS)

        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                dataSnapshot.children.forEach { orderSnapshot ->
                    val order = orderSnapshot.getValue(Order::class.java)
                    if (order != null && order.estado.equals(Estado.PENDIENTE)) {
                        orders.add(order)
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