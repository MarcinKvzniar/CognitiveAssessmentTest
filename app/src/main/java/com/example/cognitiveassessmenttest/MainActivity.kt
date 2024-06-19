package com.example.cognitiveassessmenttest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.colorMatch.ColorActivity
import com.example.cognitiveassessmenttest.memoryWords.MemoryWordsActivity
import com.example.cognitiveassessmenttest.sudoku.SudokuInstructionActivity

/**
 * This is the main activity for the application.
 * It handles navigation to the different game activities.
 */
class MainActivity : AppCompatActivity() {

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and sets up the button listeners.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize the Memory Words game button and set its click listener
        val bnMemoryWords = findViewById<Button>(R.id.bn_memory_words)
        bnMemoryWords.setOnClickListener {
            val intent = Intent(this, MemoryWordsActivity::class.java)
            startActivity(intent)
        }

        // Initialize the Color Match game button and set its click listener
        val bnColorMatch = findViewById<Button>(R.id.bn_color_match)
        bnColorMatch.setOnClickListener {
            val intent = Intent(this, ColorActivity::class.java)
            startActivity(intent)
        }

        // Initialize the Sudoku game button and set its click listener
        val bnSudoku = findViewById<Button>(R.id.bn_sudoku)
        bnSudoku.setOnClickListener {
            val intent = Intent(this, SudokuInstructionActivity::class.java)
            startActivity(intent)
        }
    }
}