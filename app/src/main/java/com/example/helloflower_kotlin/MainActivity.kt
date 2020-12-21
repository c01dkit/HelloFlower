package com.example.helloflower_kotlin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.main_activity.*
import kotlin.concurrent.thread


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var deviceDao: DeviceDao
    private val flowerList = ArrayList<FlowerData>()

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

        val mqtt =  AliMqtt("a1YFxxHqbhJ","c01dkit","1078a7226ae0ce5a657a484605b88a07",applicationContext)
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshFlowers(adapter)
        }

    }

    private fun initFlowers(){
        flowerList.clear()
        thread {
            for (device in deviceDao.loadAllDevices()){
                flowerList.add(
                    FlowerData(device.deviceName,device.devicePicture,device.groupID))
            }
        }
        flowerList.add(FlowerData("添加新的植物",R.drawable.begonia,""))
    }

    private fun refreshFlowers(adapter: FlowerAdapter) {
        thread {
            Thread.sleep(2000)
            runOnUiThread {
                initFlowers()
                adapter.notifyDataSetChanged()
                swipeRefresh.isRefreshing = false
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }




}