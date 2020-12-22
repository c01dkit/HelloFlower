package com.example.helloflower_kotlin

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FlowerAdapter(val context: Context, val flowerList: List<FlowerData>):
        RecyclerView.Adapter<FlowerAdapter.ViewHolder>(){
    private val TAG = "FlowerAdapter"
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val flowerImage: ImageView = view.findViewById(R.id.flowerImage)
        val flowerName: TextView = view.findViewById(R.id.flowerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flower_item,parent,false)
        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            if (position == itemCount-1){
                val prefs = context.getSharedPreferences(context.packageName,Context.MODE_PRIVATE)
                val ProductKey = prefs.getString("ProductKey","")
            } else{
                val flower = flowerList[position]
                val intent = Intent(context, FlowerActivity::class.java).apply {
                    putExtra(FlowerActivity.FLOWER_NAME, flower.name)
                    putExtra(FlowerActivity.FLOWER_IMAGE_ID,flower.imageId)
                }
                context.startActivity(intent)
            }


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