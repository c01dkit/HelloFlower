package com.example.helloflower_kotlin

import androidx.room.*

@Dao
interface SensorDao {
    @Insert
    fun insertSensor(sensor: Sensor): Long

    @Update
    fun updateSensor(newSensor: Sensor)

    @Delete
    fun deleteSensor(sensor: Sensor)

    @Query("select * from Sensor")
    fun loadAllSensors(): List<Sensor>

    @Query("delete from Sensor where id = :id")
    fun deleteSensorById(id: Long): Int

}