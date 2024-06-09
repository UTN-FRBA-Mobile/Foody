package ar.edu.utn.frba.foody.ui.dataBase

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

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
}