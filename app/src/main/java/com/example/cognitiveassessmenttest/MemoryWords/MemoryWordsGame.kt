package com.example.cognitiveassessmenttest.MemoryWords

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.content.Intent
import android.os.CountDownTimer
import com.example.cognitiveassessmenttest.MemoryWords.ResultActivityMemoryWords
import com.example.cognitiveassessmenttest.MemoryWords.WordList
import com.example.cognitiveassessmenttest.R


class MemoryWordsGame : AppCompatActivity() {
         lateinit var tvWordsToRemember: TextView
         lateinit var tvTimer: TextView
         lateinit var etRecalledWords: EditText
         lateinit var btnSubmit: Button

    val wordListInstance = WordList()
    val wordList = wordListInstance.words.shuffled().take(5).toMutableList()
    var currentRound = 0
    var totalWordsRemembered = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_words_game)

            tvWordsToRemember = findViewById(R.id.tvWordsToRemember)
            tvTimer = findViewById(R.id.tvTimer)
            etRecalledWords = findViewById(R.id.etRecalledWords)
            btnSubmit = findViewById(R.id.btnSubmit)

            displayWords()

            btnSubmit.setOnClickListener {
                evaluateRound()
            }
        }

         fun displayWords() {
            val wordsToShow = wordList.joinToString(", ")
            tvWordsToRemember.text = "Words to Remember: $wordsToShow"

            object : CountDownTimer(6000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val secondsLeft = millisUntilFinished / 1000
                    tvTimer.text = "Timer: $secondsLeft"
                }

                override fun onFinish() {
                    tvTimer.text = "Timer: 0"
                }
            }.start()
        }

    fun evaluateRound() {
        val recalledWords = etRecalledWords.text.toString().trim().split(", ")
        val correctlyRecalled = recalledWords.count { it in wordList }

        totalWordsRemembered += correctlyRecalled
        currentRound++

        if (currentRound < 5) {
            wordList.clear()
            wordList.addAll(wordListInstance.words.shuffled().take(5))
            displayWords()
            etRecalledWords.text.clear()
        } else {
            showResults()
        }
    }
         fun showResults() {
            val averageWordsRemembered = totalWordsRemembered / 5
            val feedback = when {
                averageWordsRemembered >= 8 -> "Excellent"
                averageWordsRemembered in 6..7 -> "Good"
                averageWordsRemembered in 4..5 -> "Average"
                else -> "Below Average"
            }

            val resultsText = "Your short-term memory performance is $feedback based on the number of words you remembered."
            val intent = Intent(this, ResultActivityMemoryWords::class.java).apply {
                putExtra("resultsText", resultsText)
            }
            startActivity(intent)
            finish()
        }
    }

