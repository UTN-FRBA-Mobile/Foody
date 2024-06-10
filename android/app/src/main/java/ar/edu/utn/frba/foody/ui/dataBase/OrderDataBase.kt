package ar.edu.utn.frba.foody.ui.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.service.autofill.UserData
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.Order
import ar.edu.utn.frba.foody.ui.Classes.OrderItemInfo
import ar.edu.utn.frba.foody.ui.Classes.User
import ar.edu.utn.frba.foody.ui.Classes.UserOrder
import ar.edu.utn.frba.foody.ui.main.OrderItem

class OrderDataBase (private var context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
){
    private val restaurantDataBase: RestaurantDataBase by lazy {
        RestaurantDataBase(context)
    }

    private val userDataBase: UserDataBase by lazy {
        UserDataBase(context)
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_GROUP)
        db.execSQL(SQL_CREATE_TABLE_GROUP_X_USER)
        db.execSQL(SQL_CREATE_TABLE_ORDER)
        db.execSQL(SQL_CREATE_TABLE_USER_ORDER)
        db.execSQL(SQL_CREATE_TABLE_ORDER_ITEM)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS orderItems")
        db.execSQL("DROP TABLE IF EXISTS userOrders")
        db.execSQL("DROP TABLE IF EXISTS orders")
        db.execSQL("DROP TABLE IF EXISTS groups_users")
        db.execSQL("DROP TABLE IF EXISTS groups")
        onCreate(db)
    }

    fun deleteAndCreateTables(userDataBase: UserDataBase) {
        val db = userDataBase.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS dish")
        db.execSQL("DROP TABLE IF EXISTS restaurant")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_GROUPS = "groups"
        private const val COLUMN_GROUPS_ID = "id"
        private const val COLUMN_GROUPS_NAME = "name"
        private const val COLUMN_GROUPS_MEMBERS_LIMIT = "membersLimit"

        private const val TABLE_GROUPS_X_USERS = "groups_users"
        private const val COLUMN_GROUPS_X_USERS_GROUP_ID = "groupId"
        private const val COLUMN_GROUPS_X_USERS_USER_ID = "userId"

        private const val TABLE_ORDERS = "orders"
        private const val COLUMN_ORDERS_ID = "id"
        private const val COLUMN_ORDERS_NAME = "name"
        private const val COLUMN_ORDERS_IN_PROGRESS = "inProgress"
        private const val COLUMN_ORDERS_DIRECTION = "direction"
        private const val COLUMN_ORDERS_ESTIMATED_HOUR = "estimatedHour"
        private const val COLUMN_ORDERS_RESTAURANT_ID = "restaurantId"
        private const val COLUMN_ORDERS_GROUP_ID = "groupId"

        private const val TABLE_USER_ORDERS = "userOrders"
        private const val COLUMN_USER_ORDERS_ID = "id"
        private const val COLUMN_USER_ORDERS_USER_ID = "userId"
        private const val COLUMN_USER_ORDERS_ORDER_ID = "orderId"

        private const val TABLE_ORDER_ITEMS = "orderItems"
        private const val COLUMN_ORDER_ITEMS_ID = "id"
        private const val COLUMN_ORDER_ITEMS_QUANTITY = "quantity"
        private const val COLUMN_ORDER_ITEMS_USER_ORDER_ID = "userOrderId"
        private const val COLUMN_ORDER_ITEMS_DISH_ID = "dishId"

        private const val SQL_CREATE_TABLE_GROUP = """
            CREATE TABLE groups (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                membersLimit INTEGER NOT NULL
            )
        """

        private const val SQL_CREATE_TABLE_GROUP_X_USER = """
            CREATE TABLE groups_users (
                groupId INTEGER PRIMARY KEY,
                userId INTEGER PRIMARY KEY,
                FOREIGN KEY(groupId) REFERENCES groups("id"),
                FOREIGN KEY(userId) REFERENCES users("id")
            )
        """

        private const val SQL_CREATE_TABLE_ORDER = """
            CREATE TABLE orders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                inProgress INTEGER NOT NULL,
                direction TEXT,
                estimatedHour TEXT,
                restaurantId INTEGER,
                groupId INTEGER,
                FOREIGN KEY(restaurantId) REFERENCES restaurant("id"),
                FOREIGN KEY(groupId) REFERENCES groups("id")
            )
        """

        private const val SQL_CREATE_TABLE_USER_ORDER = """
            CREATE TABLE userOrders (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                userId INTEGER PRIMARY KEY,
                orderId INTEGER PRIMARY KEY,
                FOREIGN KEY(userId) REFERENCES users("id"),
                FOREIGN KEY(orderId) REFERENCES orders("id")
            )
        """

        private const val SQL_CREATE_TABLE_ORDER_ITEM = """
            CREATE TABLE orderItems (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                quantity INTEGER NOT NULL,
                userOrderId INTEGER PRIMARY KEY,
                dishId INTEGER PRIMARY KEY,
                FOREIGN KEY(userOrderId) REFERENCES userOrders("id"),
                FOREIGN KEY(dishId) REFERENCES dish("id")
            )
        """
    }

    fun insertGroup(group: Group): Long {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GROUPS_NAME, group.name)
            put(COLUMN_GROUPS_MEMBERS_LIMIT, group.membersLimit)
        }
        val restaurantId = db.insert(TABLE_GROUPS, null, values)

        db.close()

        return restaurantId
    }

    fun insertUserToGroup(groupId: Int, userId: Int): Long {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GROUPS_X_USERS_GROUP_ID, groupId)
            put(COLUMN_GROUPS_X_USERS_USER_ID, userId)
        }
        val restaurantId = db.insert(TABLE_GROUPS_X_USERS, null, values)

        db.close()

        return restaurantId
    }

    fun insertOrder(order: Order, groupId: Int?): Long {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDERS_NAME, order.name)
            put(COLUMN_ORDERS_IN_PROGRESS, order.inProgress)
            put(COLUMN_ORDERS_DIRECTION, order.direction)
            put(COLUMN_ORDERS_ESTIMATED_HOUR, order.estimatedHour)
            put(COLUMN_ORDERS_RESTAURANT_ID, order.restaurant.restaurantId)
            put(COLUMN_ORDERS_GROUP_ID, groupId)
        }
        val orderId = db.insert(TABLE_ORDERS, null, values)

        db.close()

        return orderId
    }

    fun insertUserOrder(userId: Int, orderId: Int): Long {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ORDERS_USER_ID, userId)
            put(COLUMN_USER_ORDERS_ORDER_ID, orderId)
        }
        val userOrderId = db.insert(TABLE_USER_ORDERS, null, values)

        db.close()

        return userOrderId
    }

    fun insertOrderItem(orderItem: OrderItemInfo, userOrderId: Int): Long {
        val db = this.readableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDER_ITEMS_QUANTITY, orderItem.quantity)
            put(COLUMN_ORDER_ITEMS_USER_ORDER_ID, userOrderId)
            put(COLUMN_ORDER_ITEMS_DISH_ID, orderItem.dish.dishId)
        }
        val orderItemId = db.insert(TABLE_ORDER_ITEMS, null, values)

        db.close()

        return orderItemId
    }

    fun getGroups(groupId: Int?): List<Group> {
        val db = this.readableDatabase

        val cursor = if(groupId != null) {
            db.rawQuery("SELECT * FROM ${TABLE_GROUPS} WHERE ${COLUMN_GROUPS_ID} = ?", arrayOf(groupId.toString()))
        } else {
            db.rawQuery("SELECT * FROM ${TABLE_GROUPS}", null)
        }

        val groups = mutableListOf<Group>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GROUPS_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUPS_NAME))
                val membersLimit = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GROUPS_MEMBERS_LIMIT))

                val users = getUsersGroup(id)

                val group = Group(id, name, users, membersLimit)

                groups.add(group)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return groups
    }

    fun getUsersGroup(groupId: Int?): List<User> {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT gu.*"
                + "FROM ${TABLE_GROUPS_X_USERS} gu"
                + "INNER JOIN ${UserDataBase.TABLE_NAME} u ON gu.${COLUMN_GROUPS_X_USERS_USER_ID} = u.${UserDataBase.COLUMN_ID}"
                + "WHERE ${COLUMN_GROUPS_X_USERS_GROUP_ID} = ?"
            , arrayOf(groupId.toString()))

        val users = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(UserDataBase.COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(UserDataBase.COLUMN_NAME))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(UserDataBase.COLUMN_PASSWORD))
                users.add(User(id, name, password))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return users
    }

    fun getAllOrders(): List<Order> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_ORDERS", null)
        val orders = mutableListOf<Order>()

        if (cursor.moveToFirst()) {
            do {
                val orderId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_NAME))
                val inProgress = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_IN_PROGRESS))
                val direction = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_DIRECTION))
                val estimatedHour = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_ESTIMATED_HOUR))
                val restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_RESTAURANT_ID))
                val groupId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDERS_GROUP_ID))

                // Obtén los userOrders relacionados con esta orden
                val userOrdersCursor = db.rawQuery(
                    "SELECT * FROM $TABLE_USER_ORDERS WHERE $COLUMN_USER_ORDERS_ORDER_ID = ?",
                    arrayOf(orderId.toString())
                )
                val userOrders = mutableListOf<UserOrder>()

                if (userOrdersCursor.moveToFirst()) {
                    do {
                        val userOrderId = userOrdersCursor.getInt(userOrdersCursor.getColumnIndexOrThrow(COLUMN_USER_ORDERS_ID))
                        val userId = userOrdersCursor.getInt(userOrdersCursor.getColumnIndexOrThrow(COLUMN_USER_ORDERS_USER_ID))

                        // Obtén los elementos de la orden relacionados con este userOrder
                        val orderItemsCursor = db.rawQuery(
                            "SELECT * FROM $TABLE_ORDER_ITEMS WHERE $COLUMN_ORDER_ITEMS_USER_ORDER_ID = ?",
                            arrayOf(userOrderId.toString())
                        )
                        val orderItems = mutableListOf<OrderItemInfo>()

                        if (orderItemsCursor.moveToFirst()) {
                            do {
                                val orderItemId = orderItemsCursor.getInt(orderItemsCursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_ID))
                                val quantity = orderItemsCursor.getInt(orderItemsCursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_QUANTITY))
                                val dishId = orderItemsCursor.getInt(orderItemsCursor.getColumnIndexOrThrow(COLUMN_ORDER_ITEMS_DISH_ID))

                                // Obtén el dish correspondiente a este orderItem
                                val dishCursor = db.rawQuery(
                                    "SELECT * FROM $RestaurantDataBase.TABLE_DISH WHERE $RestaurantDataBase.COLUMN_DISH_ID = ?",
                                    arrayOf(dishId.toString())
                                )

                                val dish = restaurantDataBase.getDishFromCursor(dishCursor, dishId)

                                dishCursor.close()

                                orderItems.add(OrderItemInfo(orderItemId, dish, quantity))
                            } while (orderItemsCursor.moveToNext())
                        }
                        orderItemsCursor.close()

                        val user = userDataBase.getUsers(userId).first()

                        userOrders.add(UserOrder(userOrderId, orderItems, user))
                    } while (userOrdersCursor.moveToNext())
                }
                userOrdersCursor.close()

                val restaurant = restaurantDataBase.getRestaurants(userDataBase, restaurantId).first()

                val inProgressMapeado = if (inProgress == 1) true else false;

                val group = getGroups(groupId).first()
                
                orders.add(Order(orderId, restaurant, name, inProgressMapeado, userOrders, direction, estimatedHour, restaurantId, group))
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return orders
    }
}