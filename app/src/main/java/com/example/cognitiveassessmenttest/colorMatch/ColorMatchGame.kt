package com.example.cognitiveassessmenttest.colorMatch

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import java.util.Date

/**
 * This is the main activity for the Color Match game.
 * It handles the game logic, including the countdown timers, button listeners, and game end.
 */
class ColorMatchGame : AppCompatActivity() {

    // UI elements and game variables
    private lateinit var buttons: Array<Button>
    private lateinit var timerTextView: TextView
    private var colors: MutableList<Int> = mutableListOf()
    private var firstSelected: Button? = null
    private var secondSelected: Button? = null
    private var pairsFound = 0
    private var attempts = 0
    private var gameStartTime: Long = 0
    private lateinit var colorFlipTimer: CountDownTimer
    private lateinit var gameplayTimer: CountDownTimer

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and starts the game.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_match_game)

        // Initialize the timer TextView
        timerTextView = findViewById(R.id.timerTextView)

        // Initialize the buttons array with the button IDs from the XML layout
        buttons = arrayOf(
            findViewById(R.id.button1), findViewById(R.id.button2), findViewById(R.id.button3),
            findViewById(R.id.button4), findViewById(R.id.button5), findViewById(R.id.button6),
            findViewById(R.id.button7), findViewById(R.id.button8), findViewById(R.id.button9),
            findViewById(R.id.button10), findViewById(R.id.button11), findViewById(R.id.button12),
            findViewById(R.id.button13), findViewById(R.id.button14), findViewById(R.id.button15)
        )

        // Initialize colors and shuffle them
        initializeColors()
        assignColorsToButtons()

        // Start the color flip countdown
        startColorFlipCountDown()
    }

    /**
     * This function starts the color flip countdown.
     * It flips the colors of the buttons after a certain time.
     */
    private fun startColorFlipCountDown() {
        colorFlipTimer = object : CountDownTimer(5000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                timerTextView.text = "Time until cards are turned over: ${millisUntilFinished / 1000} s"
            }

            override fun onFinish() {
                timerTextView.text = "0"
                flipAllButtonsToDefault()
                startGameplayTimer()
            }
        }.start()
    }

    /**
     * This function starts the gameplay timer.
     * It counts the time elapsed since the start of the game.
     */
    private fun startGameplayTimer() {
        gameStartTime = System.currentTimeMillis()
        gameplayTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                val elapsedSeconds = ((System.currentTimeMillis() - gameStartTime) / 1000).toInt()
                timerTextView.text = "Time: $elapsedSeconds s"
            }

            override fun onFinish() {
                // This should not happen, as it's counting up indefinitely
            }
        }.start()

        // Set up button listeners after the initial countdown
        setupButtonListeners()
    }

    /**
     * This function initializes the colors for the game.
     * It creates a list of colors and shuffles them.
     */
    private fun initializeColors() {
        val colorList = listOf(
            R.color.red, R.color.blue, R.color.green, R.color.yellow,
            R.color.cyan, R.color.magenta, R.color.orange, R.color.purple,
            R.color.red, R.color.blue, R.color.green, R.color.yellow,
            R.color.cyan, R.color.magenta, R.color.orange
        )
        colors = colorList.shuffled().toMutableList()
    }

    /**
     * This function assigns the shuffled colors to the buttons.
     * It sets the background color of each button to a color from the shuffled list.
     */
    private fun assignColorsToButtons() {
        for (i in buttons.indices) {
            buttons[i].tag = colors[i]  // Store the color in the button's tag
            buttons[i].setBackgroundColor(resources.getColor(colors[i], theme))
        }
    }

    /**
     * This function flips all buttons to the default color.
     * It sets the background color of each button to the default color.
     */
    private fun flipAllButtonsToDefault() {
        for (button in buttons) {
            button.setBackgroundColor(resources.getColor(R.color.default_color, theme))
        }
    }

    /**
     * This function sets up the button listeners.
     * It assigns an onClick listener to each button.
     */
    private fun setupButtonListeners() {
        for (button in buttons) {
            button.setOnClickListener {
                onButtonClicked(button)
            }
        }
    }

    /**
     * This function is called when a button is clicked.
     * It handles the game logic when a button is clicked.
     */
    private fun onButtonClicked(button: Button) {
        if (button == firstSelected || button == secondSelected) return

        // Show the color of the clicked button
        button.setBackgroundColor(resources.getColor(button.tag as Int, theme))

        if (firstSelected == null) {
            firstSelected = button
        } else if (secondSelected == null) {
            secondSelected = button
            attempts++
            Handler().postDelayed({
                checkForMatch()
            }, 500)
        }
    }

    /**
     * This function checks if the selected buttons match.
     * If they match, it increments the pairs found and disables the buttons.
     * If they don't match, it flips the buttons back to the default color.
     */
    private fun checkForMatch() {
        if (firstSelected?.tag == secondSelected?.tag) {
            pairsFound++
            firstSelected?.isClickable = false
            secondSelected?.isClickable = false
            if (pairsFound == colors.size / 2) {
                endGame()
            }
        } else {
            firstSelected?.setBackgroundColor(resources.getColor(R.color.default_color, theme))
            secondSelected?.setBackgroundColor(resources.getColor(R.color.default_color, theme))
        }
        firstSelected = null
        secondSelected = null
    }

    /**
     * This function is called when the game ends.
     * It stops the gameplay timer, calculates the time taken, and saves the game data to Firestore.
     */
    private fun endGame() {
        // Stop the gameplay timer
        gameplayTimer.cancel()
        // Calculate the time taken to complete the game
        val timeTaken = ((System.currentTimeMillis() - gameStartTime) / 1000).toInt()

        // Determine the time category
        val timeCategory = when {
            timeTaken <= 10 -> "Very Good"
            timeTaken <= 15 -> "Average"
            else -> "Below Average"
        }

        // Determine the attempts category
        val attemptsCategory = when {
            attempts <= 10 -> "Very Good"
            attempts <= 15 -> "Average"
            else -> "Below Average"
        }

        val db = Firebase.firestore

        val userId = Firebase.auth.currentUser?.uid

        val game = hashMapOf(
            "time" to timeTaken,
            "attempts" to attempts,
        )

        val gameInstance = Date().toString()

        db.collection("users")
            .document(userId!!)
            .collection("games")
            .document("colorMatch")
            .collection("instances")
            .document(gameInstance)
            .set(game)
            .addOnSuccessListener {
                val intent = Intent(this, ResultActivityColorMatch::class.java).apply {
                    putExtra("TIME_TAKEN", timeTaken)
                    putExtra("ATTEMPTS", attempts)
                    putExtra("TIME_CATEGORY", timeCategory)
                    putExtra("ATTEMPTS_CATEGORY", attemptsCategory)
                }
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error writing document", e)
            }
    }

    /**
     * This function is called when the activity is destroyed.
     * It cancels the countdown timers.
     */
    override fun onDestroy() {
        super.onDestroy()
        colorFlipTimer.cancel()
        gameplayTimer.cancel()
    }
}