package com.example.helloflower_kotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.helloflower_kotlin.database.*
import com.example.helloflower_kotlin.network.GetInfoService
import com.example.helloflower_kotlin.network.InfoData
import com.example.helloflower_kotlin.network.ServiceCreator
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.add_new_sensor_dialog.*
import kotlinx.android.synthetic.main.flower_activity.*
import kotlinx.android.synthetic.main.flower_activity.recyclerView
import kotlinx.android.synthetic.main.flower_activity.toolbar
import kotlinx.android.synthetic.main.main_activity.*
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import kotlin.concurrent.thread

class FlowerActivity : AppCompatActivity() {
    val TAG = "Develop: FlowerActivity "
    private lateinit var sensorDao: SensorDao
    private lateinit var adapter: SensorAdapter
    private val sensorList = ArrayList<SensorData>()
    lateinit var flowerGroupId:String
    companion object {
        const val FLOWER_NAME = "flower_name"
        const val FLOWER_IMAGE_ID = "flower_image_id"
        const val FLOWER_GROUP_ID = "flower_group_id"
    }
    lateinit var mqtt: AliMqtt
    var STATE = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flower_activity)
        val flowerName = intent.getStringExtra(FLOWER_NAME) ?: ""
        val flowerImageId = intent.getIntExtra(FLOWER_IMAGE_ID,0)
        flowerGroupId = intent.getStringExtra(FLOWER_GROUP_ID) ?: ""
        sensorDao = AppDatabase.getDatabase(this).sensorDao()
        adapter = SensorAdapter(this, sensorList)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.title = flowerName
        Glide.with(this).load(flowerImageId).into(flowerImageView)



        val prefs = getSharedPreferences(resources.getString(R.string.preference_path),Context.MODE_PRIVATE)
        val productKey = prefs.getString("ProductKey","Not found")
        val deviceSecret = prefs.getString("DeviceSecret","Not found")
        val deviceName = prefs.getString("DeviceName","Not found")

        try {
            mqtt = AliMqtt(productKey!!,deviceName!!,deviceSecret!!,applicationContext)
            STATE = true
        } catch (e: Exception){
            e.printStackTrace()
        }
        // 设置接收消息回调函数
        mqtt.mqttAndroidClient?.setCallback(object : MqttCallback {
            override fun connectionLost(cause: Throwable) {
                Log.i(TAG, "connection lost")
            }

            @Throws(java.lang.Exception::class)
            override fun messageArrived(topic: String, message: MqttMessage) {
                Log.i(TAG,"topic: " + topic + ", msg: " + String(message.payload))
                refreshInformation(String(message.payload))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken) {
                Log.i(TAG, "msg delivered")
            }
        })
        // 为floating action button 添加点击事件
        setFABActions()
        // 设置页面布局
        val layoutManager = GridLayoutManager(this,2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        refreshSensors()
    }

    // 返回主页
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    // 异步刷新传感器列表卡片
    private fun refreshSensors(){
        sensorList.clear()
        thread {
            for (sensor in sensorDao.loadSensorsByGroupID(flowerGroupId)){
                sensorList.add(SensorData(sensor.itemName,"等待传感器同步",sensor.groupID,
                    sensor.remark, sensor.sensorName))
            }
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }
        }
    }
    // 更新检测卡片信息
    private fun refreshInformation(payload:String){
        for (sensor in sensorList){
           if (payload.contains(sensor.sensorName)){
               sensor.itemValue = bruteForceFor(sensor.itemName,payload)
           }
        }
        runOnUiThread {
            adapter.notifyDataSetChanged()
        }
    }

    // 根据字符串特点直接提取文本
    private fun bruteForceFor(key:String,raw:String):String{
        var result = "等待传感器同步"
        if (raw.contains(key)){
            var start = raw.indexOf(key)
            val end = raw.substring(start).indexOf("}")+start
            start += raw.substring(start, end).lastIndexOf(":")
            result = raw.substring(start+1,end)
//            Log.i(TAG, "bruteForceFor: $result")
        }
        return result
    }



    private fun setFABActions(){
        menu.setOnClickListener {
            createSensor()
        }
        menu.setOnLongClickListener {
            refreshSensors()
            Toast.makeText(this, "传感器已更新", Toast.LENGTH_SHORT).show()
            true
        }
    }

    // 添加一个传感器
    private fun createSensor(){
        val dialog = layoutInflater.inflate(R.layout.add_new_sensor_dialog,findViewById(R.id.new_sensor))
        val sensorNameEditor = dialog.findViewById<TextInputEditText>(R.id.sensor_name)
        val spinner = dialog.findViewById<Spinner>(R.id.sensor_type_spinner)
        val items = resources.getStringArray(R.array.sensor_type_array)
        val optionsAdapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,items)
        optionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = optionsAdapter

        AlertDialog.Builder(this)
            .setTitle("添加传感器")
            .setView(dialog)
            .setPositiveButton("确定") { _, _ ->
                val sensorName = sensorNameEditor.text.toString()
                val category = spinner.selectedItem.toString()
                var itemName = "NULL"
                var remark = "NULL"
                when(category){
                    "温度"-> {
                        itemName = "CurrentTemperature"
                        remark = "温度(℃)"
                    }
                    "湿度"-> {
                        itemName = "CurrentHumidity"
                        remark = "湿度(%)"
                    }
                }
                thread {
                    sensorDao.insertSensor(Sensor(flowerGroupId,sensorName,itemName,remark))
                    refreshSensors()
                }
            }
            .setNegativeButton("取消",null)
            .show()
    }
}