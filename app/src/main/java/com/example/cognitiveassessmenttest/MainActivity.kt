package com.example.cognitiveassessmenttest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.MemoryWords.Memory_words_activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bn_memory_words = findViewById<Button>(R.id.bn_memory_words)
        bn_memory_words.setOnClickListener {
            val intent = Intent(this, Memory_words_activity::class.java)
            startActivity(intent)
        }
    }


    }
