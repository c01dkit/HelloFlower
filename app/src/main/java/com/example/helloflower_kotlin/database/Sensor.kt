package com.example.helloflower_kotlin.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sensor (var groupID: String, var sensorName:String, var itemName:String, var remark: String){

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}