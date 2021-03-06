package com.example.helloflower_kotlin

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.helloflower_kotlin.network.FeedbackData
import com.example.helloflower_kotlin.network.PostFeedbackService
import com.example.helloflower_kotlin.network.ServiceCreator
import kotlinx.android.synthetic.main.feedback_activity.*
import kotlin.concurrent.thread


class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.feedback_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val feedbackService = ServiceCreator.create(PostFeedbackService::class.java)

        feedbackButton.setOnClickListener {
            val feedback = feedbackText.text.toString()
            if (feedback.isNotEmpty()){
                thread {
                    feedbackService.postFeedBack(
                        FeedbackData(
                            "佚名",
                            feedback
                        )
                    ).execute()
                }
                Toast.makeText(this, "反馈已提交", Toast.LENGTH_SHORT).show()
                feedbackText.setText("")
            } else {
                Toast.makeText(this, "输入不能为空", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->finish()
        }
        return true
    }
}