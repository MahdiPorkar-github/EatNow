package com.example.eatnow.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, exportSchema = false, entities = [Food::class])
abstract class FoodDatabase : RoomDatabase() {

    abstract val foodDao: FoodDao

    companion object {

        private var database: FoodDatabase? = null
        fun getDatabase(context: Context): FoodDatabase {

            var instance = database
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    FoodDatabase::class.java,
                    "myDatabase.db"
                ).build()
            }

            return instance
        }
    }

}