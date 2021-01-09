package com.example.helloflower_kotlin.database

import androidx.room.*
import com.example.helloflower_kotlin.database.Device

@Dao
interface DeviceDao{
    @Insert
    fun insertDevice(device: Device): Long

    @Update
    fun updateDevice(newDevice: Device)

    @Delete
    fun deleteDevice(device: Device)

    @Query("select * from Device")
    fun loadAllDevices(): List<Device>

    @Query("delete from Device where id = :id")
    fun deleteDeviceById(id: Long): Int

    @Query("delete from Device where groupID = :groupID")
    fun deleteDeviceByGroupID(groupID: String): Int

    @Query("delete from Device where deviceName = :deviceName and devicePicture = :devicePicture and groupID = :groupID")
    fun deleteDeviceByFlower(deviceName:String, devicePicture: Int, groupID: String): Int
}