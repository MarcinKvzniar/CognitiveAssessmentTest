package com.example.cognitiveassessmenttest.memoryWords

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R
import com.example.cognitiveassessmenttest.memoryWords.MemoryWordsGame

class MemoryWordsActivity : AppCompatActivity() {
    lateinit var btnStartGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_words)

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
        val intent = Intent(this, MemoryWordsGame::class.java)
        startActivity(intent)
    }
}