package com.example.helloflower_kotlin

import androidx.room.*

@Dao
interface DeviceDao {

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

}