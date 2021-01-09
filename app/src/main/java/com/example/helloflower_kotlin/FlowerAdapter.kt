package com.example.helloflower_kotlin

import android.app.Activity
import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.helloflower_kotlin.database.AppDatabase
import com.example.helloflower_kotlin.database.DeviceDao
import kotlin.concurrent.thread

class FlowerAdapter(val context: Context, val flowerList: List<FlowerData>):
        RecyclerView.Adapter<FlowerAdapter.ViewHolder>(){
    private val TAG = "FlowerAdapter"
    val sensorDao = AppDatabase.getDatabase(HelloFlowerApp.context).sensorDao()
    val deviceDao = AppDatabase.getDatabase(HelloFlowerApp.context).deviceDao()
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val flowerImage: ImageView = view.findViewById(R.id.flowerImage)
        val flowerName: TextView = view.findViewById(R.id.flowerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flower_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            val flower = flowerList[position]
            val intent = Intent(context, FlowerActivity::class.java).apply {
                putExtra(FlowerActivity.FLOWER_NAME, flower.name)
                putExtra(FlowerActivity.FLOWER_IMAGE_ID,flower.imageId)
                putExtra(FlowerActivity.FLOWER_GROUP_ID,flower.groupID)
            }
            context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            val flower = flowerList[position]
            AlertDialog.Builder(context)
                .setMessage("是否删除此植株?其下的传感器也将被删除")
                .setView(R.layout.empty_dialog)
                .setPositiveButton("确定"){_,_->
                    val groupID = flower.groupID
                    thread {
                        sensorDao.deleteSensorsByGroupID(groupID)
                        deviceDao.deleteDeviceByGroupID(groupID)
                    }

                }
                .setNegativeButton("取消",null)
                .show()
            true
        }
        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flower = flowerList[position]
        holder.flowerName.text = flower.name
        Glide.with(context).load(flower.imageId).into(holder.flowerImage)
    }

    override fun getItemCount() = flowerList.size

}