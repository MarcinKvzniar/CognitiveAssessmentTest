package com.example.cognitiveassessmenttest.ColorMatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.cognitiveassessmenttest.MainActivity
import com.example.cognitiveassessmenttest.R

class ResultActivityColorMatch : AppCompatActivity() {

    private lateinit var scoreText: TextView
    private lateinit var retryButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result_color_match)

        scoreText = findViewById(R.id.scoreText)
        retryButton = findViewById(R.id.buttonTryAgain)

        val timeTaken = intent.getLongExtra("TIME_TAKEN", 0)
        val attempts = intent.getIntExtra("ATTEMPTS", 0)

        scoreText.text = "Time: $timeTaken seconds\nAttempts: $attempts"

        retryButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
