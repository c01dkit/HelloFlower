package com.example.helloflower_kotlin

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Flow

class MainActivity : AppCompatActivity() {

    val flowers = mutableListOf(Flower("begonia",R.drawable.begonia),
        Flower("rose",R.drawable.rose), Flower("chrysanthemum",R.drawable.chrysanthemum),
        Flower("jasmine",R.drawable.jasmine), Flower("lily",R.drawable.lily),
        Flower("peachblossom",R.drawable.peachblossom), Flower("pearflower",R.drawable.pearflower),
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
            Toast.makeText(this, "功能有待开发~", Toast.LENGTH_SHORT).show()
            true
        }
        fab_like.setOnClickListener {
            Toast.makeText(this, "功能有待开发~", Toast.LENGTH_SHORT).show()
        }
        fab_light.setOnClickListener {
            Toast.makeText(this, "功能有待开发~", Toast.LENGTH_SHORT).show()
        }
        fab_water.setOnClickListener {
            Toast.makeText(this, "功能有待开发~", Toast.LENGTH_SHORT).show()
        }

        initFlowers()
        val layoutManager = GridLayoutManager(this,2)
        recyclerView.layoutManager = layoutManager
        val adapter = FlowerAdapter(this,flowerList)
        recyclerView.adapter = adapter
    }

    private fun initFlowers(){
        flowerList.clear()
        repeat(40) {
            val index = (0 until flowers.size).random()
            flowerList.add(flowers[index])
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
        }
        return true
    }
}