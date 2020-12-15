package com.example.helloflower_kotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.flower_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FlowerActivity : AppCompatActivity() {
    val TAG = "FlowerActivty"
    companion object {
        const val FLOWER_NAME = "flower_name"
        const val FLOWER_IMAGE_ID = "flower_image_id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.flower_activity)
        val flowerName = intent.getStringExtra(FLOWER_NAME) ?: ""
        val flowerImageId = intent.getIntExtra(FLOWER_IMAGE_ID,0)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingToolbar.title = flowerName
        Glide.with(this).load(flowerImageId).into(flowerImageView)
        flowerContentText.text = generateFlowerContent(flowerName)

        menu.setOnClickListener {
            val flowerService = ServiceCreator.create(GetInfoService::class.java)
            flowerService.getAppData().enqueue(object : Callback<List<InfoData>> {
                override fun onResponse(
                    call: Call<List<InfoData>>,
                    response: Response<List<InfoData>>
                ) {
                    val list = response.body()
                    if (list!=null) {
                        for (data in list) {
                            Toast.makeText(baseContext,"Hello Flower~",Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(call: Call<List<InfoData>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
        }

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