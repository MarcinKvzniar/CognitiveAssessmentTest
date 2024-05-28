package com.example.cognitiveassessmenttest.MemoryWords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.cognitiveassessmenttest.R

class ResultActivityMemoryWords : AppCompatActivity() {

    private lateinit var tvResults: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_memory_words)

        tvResults = findViewById(R.id.tvResults)

        val resultsText = intent.getStringExtra("resultsText")
        tvResults.text = resultsText
    }
    }
