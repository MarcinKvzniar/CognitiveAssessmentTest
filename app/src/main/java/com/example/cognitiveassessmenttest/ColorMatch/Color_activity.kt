package com.example.cognitiveassessmenttest.ColorMatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import com.example.cognitiveassessmenttest.MemoryWords.MemoryWordsGame
import com.example.cognitiveassessmenttest.R

class Color_activity : AppCompatActivity() {
    lateinit var btnStartGame: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color)


        btnStartGame = findViewById(R.id.btnStartGame)
        btnStartGame.setOnClickListener {
            startGame()
        }
    }

    fun startGame() {
        // Start timer before transitioning to game activity
        object : CountDownTimer(4000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                btnStartGame.text = "Starting in $secondsLeft"
            }

            override fun onFinish() {
                btnStartGame.text = "Start Game"
                navigateToGame()
            }
        }.start()
    }

    fun navigateToGame() {
        val intent = Intent(this,ColorMatchGame::class.java)
        startActivity(intent)
    }
}