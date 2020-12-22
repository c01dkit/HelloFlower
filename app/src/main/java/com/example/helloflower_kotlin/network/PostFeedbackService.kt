package com.example.helloflower_kotlin.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PostFeedbackService {
    @POST("post_feedback.php")
    fun postFeedBack(@Body data: FeedbackData): Call<ResponseBody>
}