package com.example.helloflower_kotlin

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class FlowerAdapter(val context: Context, val flowerList: List<Flower>):
        RecyclerView.Adapter<FlowerAdapter.ViewHolder>(){

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val flowerImage: ImageView = view.findViewById(R.id.flowerImage)
        val flowerName: TextView = view.findViewById(R.id.flowerName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.flower_item,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val flower = flowerList[position]
        holder.flowerName.text = flower.name
        Glide.with(context).load(flower.imageId).into(holder.flowerImage)
    }

    override fun getItemCount() = flowerList.size
}