package com.example.helloflower_kotlin.database

import androidx.room.*
import com.example.helloflower_kotlin.database.Sensor

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

    @Query("select * from Sensor where groupID = :groupID")
    fun loadSensorsByGroupID(groupID: String): List<Sensor>

    @Query("delete from Sensor where id = :id")
    fun deleteSensorById(id: Long): Int

    @Query("delete from Sensor where groupID = :groupID and itemName = :itemName")
    fun deleteSensorsByGroupIDAndItemName(groupID: String, itemName: String): Int

    @Query("delete from Sensor where groupID = :groupID")
    fun deleteSensorsByGroupID(groupID: String): Int

}