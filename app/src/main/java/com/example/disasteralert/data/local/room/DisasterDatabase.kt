package com.example.disasteralert.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.disasteralert.data.local.entity.DisasterEntity

@Database(entities = [DisasterEntity::class], version = 1, exportSchema = false)
abstract class DisasterDatabase: RoomDatabase() {
    abstract fun disasterDao(): DisasterDao

}