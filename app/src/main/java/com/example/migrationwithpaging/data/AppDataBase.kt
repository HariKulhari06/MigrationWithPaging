package com.example.migrationwithpaging.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    companion object {
        private var INSTANCE: AppDataBase? = null
        fun getDatabase(context: Context): AppDataBase? {
            if (INSTANCE == null) {
                synchronized(AppDataBase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        AppDataBase::class.java, "ranosys.db"
                    ).createFromAsset("ranosys.db").build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun movieDao(): MovieDao
}