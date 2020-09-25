package com.sunrisesunset.data.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sunrisesunset.data.network.entityModel.EntityCity

@Database(
    entities = [
        EntityCity::class
    ], version = 10, exportSchema = false
)
abstract class SunDatabase : RoomDatabase() {

    abstract val sunDatabaseDao: SunDatabaseDao

    companion object {
        @Volatile
        private var INSTANCE: SunDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(LOCK) {
            INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context,
            SunDatabase::class.java, "DatabaseSun.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
}
