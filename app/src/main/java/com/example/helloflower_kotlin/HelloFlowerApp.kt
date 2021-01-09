package com.example.helloflower_kotlin

import android.app.Application
import android.content.Context

// 便于在全应用中获取context
class HelloFlowerApp : Application(){

    companion object {
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}