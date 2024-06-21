package com.example.cognitiveassessmenttest.colorMatch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.MainActivity
import com.example.cognitiveassessmenttest.R

/**
 * This is the result activity for the Color Match game.
 * It displays the game results and provides options to retry the game or go back to the main page.
 */
class ResultActivityColorMatch : AppCompatActivity() {

    // UI elements
    private lateinit var scoreText: TextView
    private lateinit var retryButton: Button
    private lateinit var backToMainPage: Button

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and displays the game results.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_color_match)

        // Initialize the UI elements
        scoreText = findViewById(R.id.scoreText)
        retryButton = findViewById(R.id.buttonTryAgain)
        backToMainPage = findViewById(R.id.buttonBack)

        // Get the game results from the intent extras
        val timeTaken = intent.getIntExtra("TIME_TAKEN", 0)
        val attempts = intent.getIntExtra("ATTEMPTS", 0)

        // Determine the time and attempts categories
        val timeCategory = when {
            timeTaken <= 15 -> "Very Good"
            timeTaken in 16..20 -> "Average"
            else -> "Below Average"
        }

        val attemptsCategory = when {
            attempts <= 15 -> "Very Good"
            attempts in 16..20 -> "Average"
            else -> "Below Average"
        }

        // Determine the combined message based on the time and attempts categories
        val combinedMessage = when {
            timeCategory == "Very Good" && attemptsCategory == "Very Good" ->
                "Excellent! Your working memory is very good. Keep up the great work! Your time and attempts are both less than 10 seconds."
            (timeCategory == "Very Good" && attemptsCategory == "Average") ||
                    (timeCategory == "Average" && attemptsCategory == "Very Good") ->
                "Good job! Your working memory is good, but there's still room for improvement. Your performance is solid, but try to be quicker and more accurate."
            timeCategory == "Average" && attemptsCategory == "Average" ->
                "Good, your time and attempts indicate that your memory is in good condition. You're doing well, but there's room for improvement. Keep practicing!"
            timeCategory == "Below Average" || attemptsCategory == "Below Average" ->
                "Your time and attempts are higher than 15 seconds and/or more than 15 attempts. Your working memory needs improvement. Consider practicing more to enhance your skills."
            else -> ""
        }

        // Display the game results
        scoreText.text = "Time: $timeTaken seconds\nAttempts: $attempts\n\n$combinedMessage"

        // Set up the button listeners
        retryButton.setOnClickListener {
            val intent = Intent(this, ColorMatchGame::class.java)
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