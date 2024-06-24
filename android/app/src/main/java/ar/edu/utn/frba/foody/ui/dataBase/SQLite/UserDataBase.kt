package ar.edu.utn.frba.foody.ui.dataBase.SQLite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ar.edu.utn.frba.foody.ui.Classes.Address
import ar.edu.utn.frba.foody.ui.Classes.User

class UserDataBase(private var context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS users")
        db.execSQL("DROP TABLE IF EXISTS direccion")

        db.execSQL(SQL_CREATE_TABLE_USERS)
        db.execSQL(SQL_CREATE_TABLE_DIRECCION)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS users")
        onCreate(db)
    }

    fun createDataBase(dataBase: UserDataBase) {
        dataBase.writableDatabase.execSQL("DROP TABLE IF EXISTS users")
        onCreate(dataBase.writableDatabase)
    }

    fun addUser(
        dbHelper: UserDataBase,
        email: String,
        password: String,
        direccionId: Int?,
        numeroContacto: Int
    ) {
        val db = dbHelper.writableDatabase

        val values = ContentValues().apply {
            put("email", email)
            put("password", password)
            put("direccionId", direccionId)
            put("numeroContacto", numeroContacto)

        }
        db.insert("users", null, values)
    }

    fun updateUser(dbHelper: UserDataBase, newUser: User) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("email", newUser.email)
            put("password", newUser.password)
            put("numeroContacto", newUser.numeroContacto)
        }
        val whereClause= "id = ?"
        val whereArgs= arrayOf(newUser.userId.toString())
        db.update("users",values,whereClause,whereArgs)
        db.close()
    }

    fun deleteUser(email: String): Int {
        val db = this.writableDatabase
        return db.delete(TABLE_NAME, "$COLUMN_NAME = ?", arrayOf(email))
    }

    fun getAllUsers(): List<User> {
        return getUsers(null)
    }

    fun getUsers(userId: Int?): List<User> {
        val db = this.readableDatabase

        val cursor = if (userId != null) {
            db.rawQuery(
                "SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = ?",
                arrayOf(userId.toString())
            )
        } else {
            db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        }

        val users = mutableListOf<User>()

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val password = cursor.getString(cursor.getColumnIndexOrThrow("password"))
                val numeroContacto = cursor.getInt(cursor.getColumnIndexOrThrow("numeroContacto"))

                val address = Address.AddressInfo()

                users.add(User(id, email, password, address, numeroContacto))
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return users
    }

    fun addAddress(dbHelper: UserDataBase?, address: Address.AddressInfo): Int? {
        val db = dbHelper?.writableDatabase
        val values = ContentValues().apply {
            put("calle", address.calle)
            put("numero", address.numero)
            put("localidad", address.localidad)
            put("region", address.region)
            put("latitud", address.latitud)
            put("longitud", address.longitud)
        }
        return db?.insert("direccion", null, values)?.toInt()

    }
    fun updateAddress(dbHelper: UserDataBase, address: Address.AddressInfo):Int{
        val db=dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("calle",address.calle)
            put("numero", address.numero)
            put("localidad",address.localidad)
            put("region",address.region)
            put("latitud",address.latitud)
            put("longitud",address.longitud)
        }
        val whereClause= "id = ?"
        //val whereArgs= arrayOf(address.id.toString())
        val id=  1;//db.update("direccion",values,whereClause,whereArgs)
        db.close()
        return id
    }

    fun getAddress(addressId: Int): Address.AddressInfo? {
        val db = this.readableDatabase

        val cursor = if (addressId != 0) {
            db.rawQuery(
                "SELECT * FROM direccion WHERE id =?",
                arrayOf(addressId.toString())
            )
        } else {
            db.rawQuery("SELECT * FROM direccion", null)
        }
        if (cursor.moveToFirst()) {
            val calle = cursor.getString(cursor.getColumnIndexOrThrow("calle"))
            val numero = cursor.getInt(cursor.getColumnIndexOrThrow("numero"))
            val localidad = cursor.getString(cursor.getColumnIndexOrThrow("localidad"))
            val region = cursor.getString(cursor.getColumnIndexOrThrow("region"))
            val latitud = cursor.getDouble(cursor.getColumnIndexOrThrow("latitud"))
            val longitud = cursor.getDouble(cursor.getColumnIndexOrThrow("longitud"))
            cursor.close()
            db.close()
            return Address.AddressInfo(calle,numero,localidad,region,latitud,longitud)
        }
        cursor.close()
        db.close()
        return null
    }


    companion object {
        private const val SQL_CREATE_TABLE_DIRECCION = """
            CREATE TABLE direccion (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                calle TEXT NOT NULL,
                numero INTEGER NOT NULL,
                localidad TEXT NOT NULL,
                region TEXT NOT NULL,
                latitud DOUBLE NOT NULL,
                longitud DOUBLE NOT NULL
            )
        """
        private const val SQL_CREATE_TABLE_USERS = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                password TEXT NOT NULL,
                direccionId INTEGER,
                numeroContacto INTEGER,
                FOREIGN KEY(direccionId) REFERENCES direccion("id")
            )
        """


        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "users"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_PASSWORD = "password"
    }
}