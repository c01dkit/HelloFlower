package com.example.helloflower_kotlin

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.example.helloflower_kotlin.database.AppDatabase
import com.example.helloflower_kotlin.database.Device
import com.example.helloflower_kotlin.database.DeviceDao
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.main_activity.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var deviceDao: DeviceDao
    private val flowerList = ArrayList<FlowerData>()
    private var addFlag = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        deviceDao = AppDatabase.getDatabase(this).deviceDao()
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_menu)
        }

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

        initFlowers()

        val layoutManager = GridLayoutManager(this,1)
        recyclerView.layoutManager = layoutManager
        val adapter = FlowerAdapter(this,flowerList)
        recyclerView.adapter = adapter

//        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
//        swipeRefresh.setOnRefreshListener {
//            refreshFlowers(adapter)
//        }

    }

    private fun initFlowers(){
        flowerList.clear()
        thread {
            for (device in deviceDao.loadAllDevices()){
                flowerList.add(
                    FlowerData(device.deviceName,device.devicePicture,device.groupID))
            }
        }
    }
//
//    private fun refreshFlowers(adapter: FlowerAdapter) {
//        thread {
//            Thread.sleep(2000)
//            runOnUiThread {
//                initFlowers()
//                adapter.notifyDataSetChanged()
//                swipeRefresh.isRefreshing = false
//            }
//        }
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.add_new_flower->{
                val dialog = layoutInflater.inflate(R.layout.add_new_device_dialog,findViewById(R.id.new_flower))
                val groupIDEditor = dialog.findViewById<TextInputEditText>(R.id.group_id)
                val groupNameEditor = dialog.findViewById<TextInputEditText>(R.id.group_name)

                MaterialAlertDialogBuilder(this)
                    .setTitle("新建植物")
                    .setView(dialog)
                    .setPositiveButton("确定") { _, _ ->
                        thread {
                            deviceDao.insertDevice(Device(groupNameEditor.text.toString(),
                            R.drawable.begonia,groupIDEditor.text.toString(),""))
                        }
                        Toast.makeText(this@MainActivity, "添加成功",Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("取消",null)
                    .show()
                initFlowers()
            }
        }
        return true
    }




}