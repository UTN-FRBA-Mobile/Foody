package ar.edu.utn.frba.foody.ui.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Group
import ar.edu.utn.frba.foody.ui.Classes.User

class GroupDataBase(private var context: Context) : SQLiteOpenHelper(
    context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS groups")
        db.execSQL("DROP TABLE IF EXISTS users")

        db.execSQL(SQL_CREATE_TABLE_GROUPS)
        db.execSQL(SQL_CREATE_TABLE_USERS)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS groups")
        onCreate(db)
    }


    fun createDataBase(dataBase: GroupDataBase) {
        dataBase.writableDatabase.execSQL("DROP TABLE IF EXISTS groups")
        onCreate(dataBase.writableDatabase)
    }

    fun insertGroup(
        dbHelper: GroupDataBase,
        group: Group
    ) {
        val db = dbHelper.writableDatabase

        val groupValues = ContentValues().apply {
            put(COLUMN_GROUP_NAME, group.name)
            put(COLUMN_GROUP_PASSWORD, group.password)
            put(COLUMN_GROUP_MEMBERS_LIMIT, group.membersLimit)

        }
        val groupId = db.insert(TABLE_GROUP, null, groupValues)

        group.members.forEach { user: User ->
            val userValues = ContentValues().apply {
                put(COLUMN_USER_ID, user.userId)
                put(COLUMN_USER_EMAIL, user.email)
                put(COLUMN_USER_PASSWORD, user.password)
                put(COLUMN_USER_DIRECCION_ID, user.direccion)
                put(COLUMN_USER_GROUP_ID, groupId)
            }
            db.insert(TABLE_USER, null, userValues)
        }
    }

    fun getAllGroups(groupDataBase: GroupDataBase?): List<Group> {
        return getGroups(groupDataBase, null)
    }

    private fun getGroups(groupDataBase: GroupDataBase?, groupId: Int?): List<Group> {
        if (groupDataBase != null) {
            val db = groupDataBase.readableDatabase

            val cursor = if (groupId != null) {
                db.rawQuery(
                    "SELECT * FROM $TABLE_GROUP WHERE $COLUMN_GROUP_ID = ?",
                    arrayOf(groupId.toString())
                )
            } else {
                db.rawQuery("SELECT * FROM $TABLE_GROUP", null)
            }

            val groups = mutableListOf<Group>()

            if (cursor.moveToFirst()) {
                do {
                    val groupId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GROUP_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_GROUP_NAME))
                    val password = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_GROUP_PASSWORD)
                    )
                    val membersLimit =
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_GROUP_MEMBERS_LIMIT))

                    val userCursor = db.rawQuery(
                        "SELECT * FROM $TABLE_USER WHERE $COLUMN_USER_GROUP_ID = ?",
                        arrayOf(groupId.toString())
                    )
                    val users = mutableListOf<User>()

                    if (userCursor.moveToFirst()) {
                        do {

                            val userId =
                                userCursor.getInt(
                                    userCursor.getColumnIndexOrThrow(
                                        COLUMN_USER_ID
                                    )
                                )

                            val user = getUserFromCursor(userCursor, userId)

                            users.add(user)
                        } while (userCursor.moveToNext())
                    }
                    userCursor.close()

                    groups.add(Group(groupId, name, password, users, membersLimit))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return groups
        }
        return mutableListOf()
    }

    fun getUserFromCursor(userCursor: Cursor, userId: Int): User {
        val userName = userCursor.getString(userCursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL))
        val userPassword = userCursor.getString(
            userCursor.getColumnIndexOrThrow(
                COLUMN_USER_PASSWORD
            )
        )
        val userDirection = userCursor.getInt(
            userCursor.getColumnIndexOrThrow(
                COLUMN_USER_DIRECCION_ID
            )
        )
        val userGroupId = userCursor.getInt(userCursor.getColumnIndexOrThrow(COLUMN_USER_GROUP_ID))

        return User(userId, userName, userPassword, userDirection, 0, userGroupId)
    }

    companion object {
        private const val SQL_CREATE_TABLE_GROUPS = """
            CREATE TABLE groups (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                password TEXT NOT NULL,
                membersLimit INTEGER
            )
        """
        private const val SQL_CREATE_TABLE_USERS = """
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                password TEXT NOT NULL,
                direccionId INTEGER,
                numeroContacto INTEGER,
                groupId INTEGER,
                FOREIGN KEY(groupId) REFERENCES groups("id")
            )
        """

        private const val DATABASE_NAME = "mydatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_GROUP = "groups"
        const val COLUMN_GROUP_ID = "id"
        const val COLUMN_GROUP_NAME = "name"
        const val COLUMN_GROUP_PASSWORD = "password"
        const val COLUMN_GROUP_MEMBERS_LIMIT = "membersLimit"

        private const val TABLE_USER = "users"
        const val COLUMN_USER_ID = "id"
        const val COLUMN_USER_EMAIL = "email"
        const val COLUMN_USER_PASSWORD = "password"
        const val COLUMN_USER_DIRECCION_ID = "direccionId"
        const val COLUMN_USER_GROUP_ID = "groupId"
    }
}