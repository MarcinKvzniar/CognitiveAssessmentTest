package com.example.cognitiveassessmenttest.sudoku

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R

/**
 * This is the instruction activity for the Sudoku game.
 * It handles the game start and navigation to the game activity.
 */
class SudokuInstructionActivity : AppCompatActivity() {
    // Button to start the game
    lateinit var btnStartGame: Button

    /**
     * This function is called when the activity is starting.
     * It initializes the activity and sets the content view.
     */
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku_instruction)

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
        val intent = Intent(this, SudokuActivity::class.java)
        startActivity(intent)
    }
}
