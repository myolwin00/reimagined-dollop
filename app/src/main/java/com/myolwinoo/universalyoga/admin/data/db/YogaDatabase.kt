package com.myolwinoo.universalyoga.admin.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        YogaCourseEntity::class
    ],
    version = 1
)
abstract class YogaDatabase : RoomDatabase() {
    abstract fun yogaDao(): YogaDao

    companion object {
        private const val DATABASE_NAME = "yoga_db"

        fun getInstance(applicationContext: Context): YogaDatabase {
            return Room.inMemoryDatabaseBuilder(
                applicationContext,
                YogaDatabase::class.java, /*DATABASE_NAME*/
            ).build()
        }
    }

}