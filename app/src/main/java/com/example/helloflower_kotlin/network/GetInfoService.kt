package com.example.helloflower_kotlin.network

import retrofit2.Call
import retrofit2.http.GET

interface GetInfoService {
    @GET("info.php")
    fun getAppData(): Call<List<InfoData>>
}