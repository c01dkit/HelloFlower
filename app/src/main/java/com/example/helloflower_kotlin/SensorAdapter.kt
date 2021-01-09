package com.example.helloflower_kotlin

import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.helloflower_kotlin.database.AppDatabase
import kotlin.concurrent.thread

class SensorAdapter(val context: Context, val sensorList: List<SensorData>):
RecyclerView.Adapter<SensorAdapter.ViewHolder>(){
    val TAG = "SensorAdapter :"
    val sensorDao = AppDatabase.getDatabase(HelloFlowerApp.context).sensorDao()
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val itemName : TextView = view.findViewById(R.id.tv_itemName)
        val itemValue : TextView = view.findViewById(R.id.tv_itemValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SensorAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.sensor_item, parent, false)
        val holder = ViewHolder(view)
        holder.itemView.setOnLongClickListener {
            val position = holder.adapterPosition
            val sensor = sensorList[position]
            AlertDialog.Builder(context)
                .setMessage("是否删除此植株?其下的传感器也将被删除")
                .setPositiveButton("确定"){_,_->
                    thread {
                        sensorDao.deleteSensorsByGroupIDAndItemName(sensor.groupId,sensor.itemName)
                    }
                    Log.i(TAG, "onCreateViewHolder: ${sensor.groupId} ${sensor.itemName}")
                }
                .setNegativeButton("取消",null)
                .show()
            true
        }
        return holder
    }

    override fun getItemCount() = sensorList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sensor = sensorList[position]
        holder.itemName.text = sensor.remark
        holder.itemValue.text = sensor.itemValue
    }

}