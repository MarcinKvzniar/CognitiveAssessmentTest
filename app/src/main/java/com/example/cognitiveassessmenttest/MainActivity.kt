package com.example.cognitiveassessmenttest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.ColorMatch.Color_activity
import com.example.cognitiveassessmenttest.MemoryWords.Memory_words_activity
import com.example.cognitiveassessmenttest.Sudoku.SudokuActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bn_memory_words = findViewById<Button>(R.id.bn_memory_words)
        bn_memory_words.setOnClickListener {
            val intent = Intent(this, Memory_words_activity::class.java)
            startActivity(intent)
        }

        val bn_color_match = findViewById<Button>(R.id.bn_color_match)
        bn_color_match.setOnClickListener {
            val intent = Intent(this, Color_activity::class.java)
            startActivity(intent)
        }

        val bn_sudoku = findViewById<Button>(R.id.bn_sudoku)
        bn_sudoku.setOnClickListener {
            val intent = Intent(this, SudokuActivity::class.java)
            startActivity(intent)
        }
    }
}