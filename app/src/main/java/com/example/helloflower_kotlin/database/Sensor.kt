package com.example.helloflower_kotlin.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sensor (var ProductKey:String, var DeviceName: String, var DeviceSecret: String,
                   var groupID: String, var remark: String){

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}