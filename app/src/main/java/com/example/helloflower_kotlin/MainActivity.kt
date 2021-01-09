package com.example.helloflower_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.helloflower_kotlin.database.AppDatabase
import com.example.helloflower_kotlin.database.Device
import com.example.helloflower_kotlin.database.DeviceDao
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.main_activity.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var deviceDao: DeviceDao
    private lateinit var adapter: FlowerAdapter
    private val flowerList = ArrayList<FlowerData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        deviceDao = AppDatabase.getDatabase(this).deviceDao()
        adapter = FlowerAdapter(this,flowerList)

        // 设置下拉刷新
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshFlowers(adapter)
        }

        // 设置标题栏
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        // 设置滑动菜单子选项监听事件
        navView.setNavigationItemSelectedListener {
            when (it.itemId){
                R.id.navSelf->{
                    drawerLayout.closeDrawers()
                }
                R.id.navInfo->{
                    val intent = Intent(this,FeedbackActivity::class.java)
                    startActivity(intent)
                }
                R.id.navSettings->{
                    val intent = Intent(this,SettingsActivity::class.java)
                    startActivity(intent)
                }
            }
            true
        }

        // 设置页面布局
        val layoutManager = GridLayoutManager(this,1)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter


    }

    override fun onResume() {
        super.onResume()
        // 初始化页面
        refreshFlowers(adapter)
    }

    // 异步刷新植株
    private fun refreshFlowers(adapter: FlowerAdapter) {
        flowerList.clear()
        thread {
            for (device in deviceDao.loadAllDevices()){
                flowerList.add(
                    FlowerData(device.deviceName,device.devicePicture,device.groupID))
            }
            runOnUiThread {
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }

    // 引入右上角菜单选项
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // 设置按钮动作
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.add_new_flower-> createFlower()
        }
        return true
    }

    // 添加一株植物
    private fun createFlower(){
        val dialog = layoutInflater.inflate(R.layout.add_new_device_dialog,findViewById(R.id.new_flower))
        val groupIDEditor = dialog.findViewById<TextInputEditText>(R.id.group_id)
        val groupNameEditor = dialog.findViewById<TextInputEditText>(R.id.group_name)
        val flowers = mutableListOf(R.drawable.begonia,R.drawable.chrysanthemum,
            R.drawable.jasmine,R.drawable.lily,R.drawable.mint,R.drawable.peachblossom,
            R.drawable.pearblossom,R.drawable.rose)
        val randomIndex = flowers.random()

        AlertDialog.Builder(this)
            .setTitle("新建植物")
            .setView(dialog)
            .setPositiveButton("确定") { _, _ ->
                thread {
                    deviceDao.insertDevice(Device(groupNameEditor.text.toString(),
                        randomIndex,groupIDEditor.text.toString(),""))
                    refreshFlowers(adapter)
                }
                Toast.makeText(this@MainActivity, "添加成功",Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("取消",null)
            .show()
    }


}