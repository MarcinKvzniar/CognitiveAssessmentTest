package com.example.cognitiveassessmenttest.memoryWords

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R
import com.example.cognitiveassessmenttest.memoryWords.MemoryWordsGame

/**
 * This is the main activity for the Memory Words game.
 * It handles the game start and navigation to the game activity.
 */
class MemoryWordsActivity : AppCompatActivity() {
    // Button to start the game
    lateinit var btnStartGame: Button

    /**
     * This function is called when the activity is starting.
     * It initializes the activity and sets the content view.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_words)

        // Initialize the start game button
        btnStartGame = findViewById(R.id.btnStartGame)

        // Set an onClick listener to the start game button
        btnStartGame.setOnClickListener {
            startGame()
        }
    }

    /**
     * This function starts the game.
     * It starts a countdown before transitioning to the game activity.
     */
    private fun startGame() {
        // Start timer before transitioning to game activity
        object : CountDownTimer(4000, 1000) {
            // This function is called on every tick of the timer
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                btnStartGame.text = "Starting in $secondsLeft"
            }

            // This function is called when the timer is finished
            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                btnStartGame.text = "Start Game"
                navigateToGame()
            }
        }.start()
    }

    /**
     * This function navigates to the game activity.
     */
    fun navigateToGame() {
        val intent = Intent(this, MemoryWordsGame::class.java)
        startActivity(intent)
    }
}