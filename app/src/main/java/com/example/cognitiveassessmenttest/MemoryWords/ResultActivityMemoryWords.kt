package com.example.cognitiveassessmenttest.MemoryWords

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.cognitiveassessmenttest.MainActivity
import com.example.cognitiveassessmenttest.R

class ResultActivityMemoryWords : AppCompatActivity() {

    private lateinit var tvResults: TextView
    private lateinit var buttonBack: Button
    private lateinit var buttonTryAgain: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_memory_words)

        tvResults = findViewById(R.id.tvResults)
        buttonBack = findViewById(R.id.buttonBack)
        buttonTryAgain = findViewById(R.id.buttonTryAgain)

        val resultsText = intent.getStringExtra("resultsText")
        tvResults.text = resultsText


        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonTryAgain.setOnClickListener {
            val intent = Intent(this, Memory_words_activity::class.java)
            startActivity(intent)
        }
    }
}

