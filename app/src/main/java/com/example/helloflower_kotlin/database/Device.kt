package com.example.helloflower_kotlin.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Device (var deviceName: String, var devicePicture: Int,
                   var groupID: String, var remark: String){

    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}