package ar.edu.utn.frba.foody.ui.dataBase

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ar.edu.utn.frba.foody.ui.Classes.User

class UserDataBase (private var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USERS)
        db.execSQL(SQL_CREATE_TABLE_RESTAURANT)
        db.execSQL(SQL_CREATE_TABLE_DISH)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS dish")
        db.execSQL("DROP TABLE IF EXISTS restaurant")
        onCreate(db)
    }

    fun addUser(dbHelper: SQLiteOpenHelper, name:String, password:String){
        val db = dbHelper.writableDatabase
        val sql = "INSERT INTO users (name, password) VALUES (?, ?)"
        val stmt = db.compileStatement(sql)
        stmt.bindString(1, name)
        stmt.bindString(2, password)
        stmt.executeInsert()
    }
    fun deleteUser(name: String):Int{
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(name))
    }
    fun getAllUsers(): List<User> {
        val users = mutableListOf<User>()
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PASSWORD))
                users.add(User(id, name, password))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return users
    }


    companion object {
        private const val SQL_CREATE_TABLE_USERS = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                password TEXT NOT NULL
            )
        """
        private const val SQL_CREATE_TABLE_RESTAURANT = """
            CREATE TABLE restaurant (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                imageDescription TEXT,
                image INTEGER
            )
        """

        private const val SQL_CREATE_TABLE_DISH = """
            CREATE TABLE dish (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                description TEXT,
                price DOUBLE,
                imageId INTEGER,
                FOREIGN KEY(restaurantId) REFERENCES restaurant("id")
            )
        """
        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NAME = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_PASSWORD = "password"
    }
}