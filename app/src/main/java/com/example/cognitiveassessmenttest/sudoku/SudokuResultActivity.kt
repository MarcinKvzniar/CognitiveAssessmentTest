package com.example.cognitiveassessmenttest.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.MainActivity
import com.example.cognitiveassessmenttest.R
import com.example.cognitiveassessmenttest.colorMatch.ColorMatchGame

/**
 * This is the result activity for the Sudoku game.
 * It displays the game results and provides options to retry the game or go back to the main page.
 */
class SudokuResultActivity : AppCompatActivity() {
    // UI elements
    private lateinit var scoreText: TextView
    private lateinit var retryButton: Button
    private lateinit var backToMainPage: Button

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and displays the game results.
     */
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_color_match)

        // Initialize the UI elements
        scoreText = findViewById(R.id.scoreText)
        retryButton = findViewById(R.id.buttonTryAgain)
        backToMainPage = findViewById(R.id.buttonBack)

        // Get the game results from the intent extras
        val timeTaken = intent.getIntExtra("TIME_TAKEN", 0)

        // Determine the time category based on the time taken
        val timeCategory = when {
            timeTaken <= 60 -> "Very Good"
            timeTaken in 61..120 -> "Average"
            else -> "Below Average"
        }

        // Generate a feedback message based on the time category
        val combinedMessage = when {
            timeCategory == "Very Good"  ->
                "Excellent! Your cognitive abilities are very good. Keep up the great work! Your time is less than 60 seconds."
            (timeCategory == "Average") ->
                "Good, your time indicates that your cognitive abilities are in good condition. You're doing well, but there's room for improvement. Keep practicing!"
            timeCategory == "Below Average" ->
                "Your time is higher than 2 minutes. Your cognitive abilities need improvement. Consider practicing more to enhance your skills."
            else -> ""
        }

        // Display the game results and feedback message
        scoreText.text = "Time: $timeTaken seconds\n$combinedMessage"

        // Set up the button listeners
        retryButton.setOnClickListener {
            val intent = Intent(this, SudokuInstructionActivity::class.java)
            startActivity(intent)
            finish()
        }

        backToMainPage.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}