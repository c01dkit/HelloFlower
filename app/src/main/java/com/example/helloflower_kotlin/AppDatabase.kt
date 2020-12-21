package com.example.helloflower_kotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(version = 1, entities = [Sensor::class,Device::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun sensorDao(): SensorDao
    abstract fun deviceDao(): DeviceDao

    companion object{
        private var instance: AppDatabase? = null

        @Synchronized
        fun getDatabase(context: Context):AppDatabase{
            instance?.let { return it }
            return Room.databaseBuilder(context.applicationContext,
            AppDatabase::class.java,"app_database").build().apply { instance = this }
        }
    }
}