package com.example.helloflower_kotlin

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    val flowers = mutableListOf(Flower("begonia",R.drawable.begonia),
        Flower("rose",R.drawable.rose), Flower("chrysanthemum",R.drawable.chrysanthemum),
        Flower("jasmine",R.drawable.jasmine), Flower("lily",R.drawable.lily),
        Flower("peachblossom",R.drawable.peachblossom), Flower("pearblossom",R.drawable.pearblossom),
        Flower("mint",R.drawable.mint))

    val flowerList = ArrayList<Flower>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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

                }
            }
            true
        }

        initFlowers()
        val layoutManager = GridLayoutManager(this,2)
        recyclerView.layoutManager = layoutManager
        val adapter = FlowerAdapter(this,flowerList)
        recyclerView.adapter = adapter

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary)
        swipeRefresh.setOnRefreshListener {
            refreshFlowers(adapter)
        }
    }

    private fun initFlowers(){
        flowerList.clear()
        repeat(40) {
            val index = (0 until flowers.size).random()
            flowerList.add(flowers[index])
        }
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