package com.example.cognitiveassessmenttest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.colorMatch.ColorActivity
import com.example.cognitiveassessmenttest.memoryWords.MemoryWordsActivity
import com.example.cognitiveassessmenttest.sudoku.SudokuInstructionActivity

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bnMemoryWords = findViewById<Button>(R.id.bn_memory_words)
        bnMemoryWords.setOnClickListener {
            val intent = Intent(this, MemoryWordsActivity::class.java)
            startActivity(intent)
        }

        val bnColorMatch = findViewById<Button>(R.id.bn_color_match)
        bnColorMatch.setOnClickListener {
            val intent = Intent(this, ColorActivity::class.java)
            startActivity(intent)
        }

        val bnSudoku = findViewById<Button>(R.id.bn_sudoku)
        bnSudoku.setOnClickListener {
            val intent = Intent(this, SudokuInstructionActivity::class.java)
            startActivity(intent)
        }
    }
}