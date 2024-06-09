package ar.edu.utn.frba.foody.ui.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ar.edu.utn.frba.foody.ui.Classes.Group

class OrderDataBase (private var context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
){
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

    //TODO: que no reciba UserDataBase
    fun insertGroup(group: Group, userDataBase: UserDataBase): Long {
        val db = userDataBase.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GROUPS_NAME, group.name)
            put(COLUMN_GROUPS_MEMBERS_LIMIT, group.membersLimit)
        }
        val restaurantId = db.insert(TABLE_GROUPS, null, values)

        db.close()

        return restaurantId
    }

    fun insertUserToGroup(groupId: Int, userId: Int, userDataBase: UserDataBase): Long {
        val db = userDataBase.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_GROUPS_X_USERS_GROUP_ID, groupId)
            put(COLUMN_GROUPS_X_USERS_USER_ID, userId)
        }
        val restaurantId = db.insert(TABLE_GROUPS_X_USERS, null, values)

        db.close()

        return restaurantId
    }

    fun insertOrder(name: String, inProgress: Int, direction: String?, estimatedHour: String?, restaurantId: Int?, groupId: Int?, userDataBase: UserDataBase): Long {
        val db = userDataBase.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDERS_NAME, name)
            put(COLUMN_ORDERS_IN_PROGRESS, inProgress)
            put(COLUMN_ORDERS_DIRECTION, direction)
            put(COLUMN_ORDERS_ESTIMATED_HOUR, estimatedHour)
            put(COLUMN_ORDERS_RESTAURANT_ID, restaurantId)
            put(COLUMN_ORDERS_GROUP_ID, groupId)
        }
        val orderId = db.insert(TABLE_ORDERS, null, values)

        db.close()

        return orderId
    }

    fun insertUserOrder(userId: Int, orderId: Int, userDataBase: UserDataBase): Long {
        val db = userDataBase.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_ORDERS_USER_ID, userId)
            put(COLUMN_USER_ORDERS_ORDER_ID, orderId)
        }
        val userOrderId = db.insert(TABLE_USER_ORDERS, null, values)

        db.close()

        return userOrderId
    }

    fun insertOrderItem(quantity: Int, userOrderId: Int, dishId: Int, userDataBase: UserDataBase): Long {
        val db = userDataBase.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_ORDER_ITEMS_QUANTITY, quantity)
            put(COLUMN_ORDER_ITEMS_USER_ORDER_ID, userOrderId)
            put(COLUMN_ORDER_ITEMS_DISH_ID, dishId)
        }
        val orderItemId = db.insert(TABLE_ORDER_ITEMS, null, values)

        db.close()

        return orderItemId
    }
}