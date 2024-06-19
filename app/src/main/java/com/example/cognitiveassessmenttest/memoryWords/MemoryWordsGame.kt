package com.example.cognitiveassessmenttest.memoryWords

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.Date

/**
 * This is the main activity for the Memory Words game.
 * It handles the game logic, including the countdown timers, button listeners, and game end.
 */
class MemoryWordsGame : AppCompatActivity() {
    // UI elements and game variables
    private lateinit var tvWordsToRemember: TextView
    private lateinit var tvTimer: TextView
    private lateinit var etRecalledWords: EditText
    private lateinit var btnSubmit: Button
    private val wordListInstance = WordList()
    private var wordList = mutableListOf<String>()
    private var currentRound = 0
    private var totalWordsRemembered = 0
    private var countDownTimer: CountDownTimer? = null

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and starts the game.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_words_game)

        // Initialize the UI elements
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

    /**
     * This function is called when the activity state is saved.
     * It saves the current round, total words remembered, and word list.
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_CURRENT_ROUND, currentRound)
        outState.putInt(KEY_TOTAL_REMEMBERED, totalWordsRemembered)
        outState.putStringArrayList(KEY_WORD_LIST, ArrayList(wordList))
    }

    /**
     * This function starts a new game.
     * It clears the word list, adds new words, and displays them.
     */
    private fun startNewGame() {
        wordList.clear()
        wordList.addAll(wordListInstance.words.shuffled().take(3))
        displayWords()
    }

    /**
     * This function displays the words to remember.
     * It starts a countdown timer and hides the words when the timer finishes.
     */
    @SuppressLint("SetTextI18n")
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

    /**
     * This function is called when the submit button is clicked.
     * It evaluates the round and either starts a new round or ends the game.
     */
    private fun evaluateRound() {
        val recalledWords = etRecalledWords.text.toString().trim()
            .split("\\s+|,\\s*".toRegex())
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

    /**
     * This function is called when the game ends.
     * It calculates the average words remembered, provides feedback, and saves the game data to Firestore.
     */
    private fun showResults() {
        val averageWordsRemembered = totalWordsRemembered / 5f
        val feedback = when {
            averageWordsRemembered >= 2.5f -> "Excellent"
            averageWordsRemembered >= 2.0f -> "Good"
            averageWordsRemembered >= 1.5f -> "Average"
            else -> "Below Average"
        }

        val resultsText = "Your short-term memory performance is $feedback based on the number of words you remembered."

        val db = Firebase.firestore
        val userId = Firebase.auth.currentUser?.uid

        val game = hashMapOf(
            "avgWordsRemembered" to averageWordsRemembered,
        )

        val gameInstance = Date().toString()

        db.collection("users")
            .document(userId!!)
            .collection("games")
            .document("memoryWords")
            .collection("instances")
            .document(gameInstance)
            .set(game)
            .addOnSuccessListener {
                val intent = Intent(this, ResultActivityMemoryWords::class.java).apply {
                    putExtra("resultsText", resultsText)
                }
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(ContentValues.TAG, "Error writing document", e)
            }
    }

    companion object {
        private const val KEY_CURRENT_ROUND = "currentRound"
        private const val KEY_TOTAL_REMEMBERED = "totalWordsRemembered"
        private const val KEY_WORD_LIST = "wordList"
    }
}