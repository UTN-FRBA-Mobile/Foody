package ar.edu.utn.frba.foody.ui.dataBase

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import ar.edu.utn.frba.foody.ui.Classes.Dish
import ar.edu.utn.frba.foody.ui.Classes.Restaurant

class RestaurantDataBase (private var context: Context) : SQLiteOpenHelper(context,
    DATABASE_NAME, null,
    DATABASE_VERSION
) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_RESTAURANT)
        db.execSQL(SQL_CREATE_TABLE_DISH)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS dish")
        db.execSQL("DROP TABLE IF EXISTS restaurant")
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

    private const val TABLE_RESTAURANT = "restaurant"
    private const val COLUMN_RESTAURANT_ID = "id"
    private const val COLUMN_RESTAURANT_NAME = "name"
    private const val COLUMN_RESTAURANT_IMAGE_DESCRIPTION = "imageDescription"
    private const val COLUMN_RESTAURANT_IMAGE = "image"

    private const val TABLE_DISH = "dish"
    const val COLUMN_DISH_ID = "id"
    const val COLUMN_DISH_RESTAURANT_ID = "restaurantId"
    const val COLUMN_DISH_NAME = "name"
    const val COLUMN_DISH_DESCRIPTION = "description"
    const val COLUMN_DISH_PRICE = "price"
    const val COLUMN_DISH_IMAGE_RESOURCE_ID = "imageId"


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
                restaurantId INTEGER,
                name TEXT NOT NULL,
                description TEXT,
                price DOUBLE,
                imageId INTEGER,
                FOREIGN KEY(restaurantId) REFERENCES restaurant("id")
            )
        """
}

    fun insertRestaurant(restaurant: Restaurant,userDataBase: UserDataBase): Long {
        val db = userDataBase.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_RESTAURANT_NAME, restaurant.name)
            put(COLUMN_RESTAURANT_IMAGE_DESCRIPTION, restaurant.imageDescription)
            put(COLUMN_RESTAURANT_IMAGE, restaurant.image)
        }
        val restaurantId = db.insert(TABLE_RESTAURANT, null, values)

        restaurant.dishes.forEach { dish:Dish ->
            val dishValues = ContentValues().apply {
                put(COLUMN_DISH_RESTAURANT_ID, restaurantId)
                put(COLUMN_DISH_NAME, dish.name)
                put(COLUMN_DISH_DESCRIPTION, dish.description)
                put(COLUMN_DISH_PRICE,dish.price)
                put(COLUMN_DISH_IMAGE_RESOURCE_ID,dish.imageResourceId)
            }
            db.insert(TABLE_DISH, null, dishValues)
        }

        db.close()
        return restaurantId
    }

    fun getAllRestaurants(userDataBase: UserDataBase?): List<Restaurant> {
        return getRestaurants(userDataBase, null)
    }
    
    fun getRestaurants(userDataBase: UserDataBase?, restaurantId: Int?): List<Restaurant> {
        if(userDataBase!=null) {
            val db = userDataBase.readableDatabase

            val cursor = if(restaurantId != null) {
                db.rawQuery(
                    "SELECT * FROM $TABLE_RESTAURANT WHERE $COLUMN_RESTAURANT_ID = ?",
                    arrayOf(restaurantId.toString())
                )
            } else {
                db.rawQuery("SELECT * FROM $TABLE_RESTAURANT", null)
            }

            val restaurants = mutableListOf<Restaurant>()

            if (cursor.moveToFirst()) {
                do {
                    val restaurantId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_ID))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_NAME))
                    val imageDescription = cursor.getString(
                        cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_IMAGE_DESCRIPTION)
                    )
                    val image = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RESTAURANT_IMAGE))

                    val dishesCursor = db.rawQuery(
                        "SELECT * FROM $TABLE_DISH WHERE $COLUMN_DISH_RESTAURANT_ID = ?",
                        arrayOf(restaurantId.toString())
                    )
                    val dishes = mutableListOf<Dish>()

                    if (dishesCursor.moveToFirst()) {
                        do {

                            val dishId =
                                dishesCursor.getInt(dishesCursor.getColumnIndexOrThrow(COLUMN_DISH_ID))

                            val dish = getDishFromCursor(dishesCursor, dishId)

                            dishes.add(dish)
                        } while (dishesCursor.moveToNext())
                    }
                    dishesCursor.close()

                    restaurants.add(Restaurant(restaurantId, name, imageDescription, image, dishes))
                } while (cursor.moveToNext())
            }
            cursor.close()
            db.close()
            return restaurants
        }
        return mutableListOf()
    }

    fun getDishFromCursor(dishCursor: Cursor, dishId: Int): Dish {
        val dishName = dishCursor.getString(dishCursor.getColumnIndexOrThrow(COLUMN_DISH_NAME))
        val dishDescription = dishCursor.getString(dishCursor.getColumnIndexOrThrow(COLUMN_DISH_DESCRIPTION))
        val dishPrice = dishCursor.getDouble(dishCursor.getColumnIndexOrThrow(COLUMN_DISH_PRICE))
        val dishImageId = dishCursor.getInt(dishCursor.getColumnIndexOrThrow(COLUMN_DISH_IMAGE_RESOURCE_ID))
        val dishRestaurantId = dishCursor.getInt(dishCursor.getColumnIndexOrThrow(COLUMN_DISH_RESTAURANT_ID))

        return Dish(dishId, dishName, dishDescription, dishPrice, dishImageId, dishRestaurantId)
    }
}