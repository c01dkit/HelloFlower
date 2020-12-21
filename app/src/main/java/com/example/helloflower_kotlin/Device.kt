package com.example.helloflower_kotlin

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device (var ProductKey:String, var DeviceName: String,
                   var DeviceSecret: String, var remark: String){

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}