package com.example.helloflower_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_flower.*

class FlowerActivity : AppCompatActivity() {

    companion object {
        const val FLOWER_NAME = "flower_name"
        const val FLOWER_IMAGE_ID = "flower_image_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flower)
        val flowerName = intent.getStringExtra(FLOWER_NAME) ?: ""
        val flowerImageId = intent.getIntExtra(FLOWER_IMAGE_ID,0)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.title = flowerName
        Glide.with(this).load(flowerImageId).into(flowerImageView)
        flowerContentText.text = generateFlowerContent(flowerName)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun generateFlowerContent(flowerName: String) = flowerName.repeat(500)
}