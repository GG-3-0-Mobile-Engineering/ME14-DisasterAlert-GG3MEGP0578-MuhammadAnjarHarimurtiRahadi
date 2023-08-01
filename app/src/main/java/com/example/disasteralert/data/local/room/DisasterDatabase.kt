package com.example.disasteralert.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.disasteralert.data.local.entity.DisasterEntity

@Database(entities = [DisasterEntity::class], version = 1, exportSchema = false)
abstract class DisasterDatabase: RoomDatabase() {
    abstract fun disasterDao(): DisasterDao

    companion object {
        @Volatile
        private var instance: DisasterDatabase? = null
        fun getInstance(context: Context): DisasterDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DisasterDatabase::class.java, "Disaster.db"
                ).build()
            }
    }
}