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

class SudokuResultActivity : AppCompatActivity() {
    private lateinit var scoreText: TextView
    private lateinit var retryButton: Button
    private lateinit var backToMainPage: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_color_match)

        scoreText = findViewById(R.id.scoreText)
        retryButton = findViewById(R.id.buttonTryAgain)
        backToMainPage = findViewById(R.id.buttonBack)

        val timeTaken = intent.getIntExtra("TIME_TAKEN", 0)

        val timeCategory = when {
            timeTaken <= 60 -> "Very Good"
            timeTaken in 61..120 -> "Average"
            else -> "Below Average"
        }

        val combinedMessage = when {
            timeCategory == "Very Good"  ->
                "Excellent! Your working memory is very good. Keep up the great work! Your time is less than 60 seconds."
            (timeCategory == "Average") ->
                "Good, your time indicates that your cognitive abilities are in good condition. You're doing well, but there's room for improvement. Keep practicing!"
            timeCategory == "Below Average" ->
                "Your time is higher than 2 minutes. Your cognitive abilities need improvement. Consider practicing more to enhance your skills."
            else -> ""
        }


        scoreText.text = "Time: $timeTaken seconds\n$combinedMessage"

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