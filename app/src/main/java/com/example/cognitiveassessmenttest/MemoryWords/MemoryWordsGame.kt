package com.example.cognitiveassessmenttest.MemoryWords

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.MemoryWords.ResultActivityMemoryWords
import com.example.cognitiveassessmenttest.MemoryWords.WordList
import com.example.cognitiveassessmenttest.R

class MemoryWordsGame : AppCompatActivity() {
    private lateinit var tvWordsToRemember: TextView
    private lateinit var tvTimer: TextView
    private lateinit var etRecalledWords: EditText
    private lateinit var btnSubmit: Button

    private val wordListInstance = WordList()
    private var wordList = mutableListOf<String>()
    private var currentRound = 0
    private var totalWordsRemembered = 0
    private var countDownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_words_game)

        tvWordsToRemember = findViewById(R.id.tvWordsToRemember)
        tvTimer = findViewById(R.id.tvTimer)
        etRecalledWords = findViewById(R.id.etRecalledWords)
        btnSubmit = findViewById(R.id.btnSubmit)

        etRecalledWords.isEnabled = false // Disable input until timer finishes
        btnSubmit.isEnabled = false // Disable submit button until timer finishes

        savedInstanceState?.let {
            currentRound = it.getInt(KEY_CURRENT_ROUND, 0)
            totalWordsRemembered = it.getInt(KEY_TOTAL_REMEMBERED, 0)
            wordList = it.getStringArrayList(KEY_WORD_LIST)?.toMutableList() ?: mutableListOf()
            displayWords()
        } ?: run {
            startNewGame()
        }

        btnSubmit.setOnClickListener {
            evaluateRound()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_ROUND, currentRound)
        outState.putInt(KEY_TOTAL_REMEMBERED, totalWordsRemembered)
        outState.putStringArrayList(KEY_WORD_LIST, ArrayList(wordList))
    }

    private fun startNewGame() {
        wordList.clear()
        wordList.addAll(wordListInstance.words.shuffled().take(3))
        displayWords()
    }

    private fun displayWords() {
        val wordsToShow = wordList.joinToString(", ")
        tvWordsToRemember.text = "Words to Remember: $wordsToShow"

        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(6000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                tvTimer.text = "Timer: $secondsLeft"
            }

            override fun onFinish() {
                tvTimer.text = "Timer: 0"
                tvWordsToRemember.text = "" // Hide words
                etRecalledWords.isEnabled = true // Enable input
                btnSubmit.isEnabled = true // Enable submit button
            }
        }.start()
    }

    private fun evaluateRound() {
        val recalledWords = etRecalledWords.text.toString().trim()
            .split("\\s*,\\s*|\\s+".toRegex())
            .map { it.lowercase() }
        val correctlyRecalled = recalledWords.count { it in wordList.map { word -> word.lowercase() } }

        totalWordsRemembered += correctlyRecalled
        currentRound++

        if (currentRound < 5) {
            wordList.clear()
            wordList.addAll(wordListInstance.words.shuffled().take(3))
            displayWords()
            etRecalledWords.text.clear()
            etRecalledWords.isEnabled = false
            btnSubmit.isEnabled = false
        } else {
            showResults()
        }
    }

    private fun showResults() {
        val averageWordsRemembered = totalWordsRemembered.toFloat() / 15 // Calculate based on 15 words total (3 words x 5 rounds)
        val feedback = when {
            averageWordsRemembered >= 8f / 3f -> "Excellent"
            averageWordsRemembered >= 6f / 3f -> "Good"
            averageWordsRemembered >= 4f / 3f -> "Average"
            else -> "Below Average"
        }

        val resultsText = "Your short-term memory performance is $feedback based on the number of words you remembered."
        val intent = Intent(this, ResultActivityMemoryWords::class.java).apply {
            putExtra("resultsText", resultsText)
        }
        startActivity(intent)
        finish()
    }

    companion object {
        private const val KEY_CURRENT_ROUND = "currentRound"
        private const val KEY_TOTAL_REMEMBERED = "totalWordsRemembered"
        private const val KEY_WORD_LIST = "wordList"
    }
}