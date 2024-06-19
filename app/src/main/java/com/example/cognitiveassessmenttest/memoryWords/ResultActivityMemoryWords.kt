package com.example.cognitiveassessmenttest.memoryWords

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.MainActivity
import com.example.cognitiveassessmenttest.R

/**
 * This is the result activity for the Memory Words game.
 * It displays the game results and provides options to retry the game or go back to the main page.
 */
class ResultActivityMemoryWords : AppCompatActivity() {

    // UI elements
    private lateinit var tvResults: TextView
    private lateinit var buttonBack: Button
    private lateinit var buttonTryAgain: Button

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and displays the game results.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_memory_words)

        // Initialize the UI elements
        tvResults = findViewById(R.id.tvResults)
        buttonBack = findViewById(R.id.buttonBack)
        buttonTryAgain = findViewById(R.id.buttonTryAgain)

        // Get the game results from the intent extras
        val resultsText = intent.getStringExtra("resultsText")
        tvResults.text = resultsText

        // Set up the button listeners
        buttonBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonTryAgain.setOnClickListener {
            val intent = Intent(this, MemoryWordsActivity::class.java)
            startActivity(intent)
        }
    }
}