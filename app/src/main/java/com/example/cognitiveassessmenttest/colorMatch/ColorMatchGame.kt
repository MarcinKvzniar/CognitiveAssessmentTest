package com.example.cognitiveassessmenttest.colorMatch

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R

class ColorMatchGame : AppCompatActivity() {

    private lateinit var buttons: Array<Button>
    private lateinit var timerTextView: TextView
    private var colors: MutableList<Int> = mutableListOf()
    private var firstSelected: Button? = null
    private var secondSelected: Button? = null
    private var pairsFound = 0
    private var attempts = 0
    private var startTime: Long = 0
    private var gameStartTime: Long = 0
    private lateinit var colorFlipTimer: CountDownTimer
    private lateinit var gameplayTimer: CountDownTimer

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

    private fun startColorFlipCountDown() {
        colorFlipTimer = object : CountDownTimer(5000, 1000) {
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

    private fun startGameplayTimer() {
        gameStartTime = System.currentTimeMillis()
        gameplayTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
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

    private fun initializeColors() {
        val colorList = listOf(
            R.color.red, R.color.blue, R.color.green, R.color.yellow,
            R.color.cyan, R.color.magenta, R.color.orange, R.color.purple,
            R.color.red, R.color.blue, R.color.green, R.color.yellow,
            R.color.cyan, R.color.magenta, R.color.orange
        )
        colors = colorList.shuffled().toMutableList()
    }

    // Assign the shuffled colors to the buttons
    private fun assignColorsToButtons() {
        for (i in buttons.indices) {
            buttons[i].tag = colors[i]  // Store the color in the button's tag
            buttons[i].setBackgroundColor(resources.getColor(colors[i], theme))
        }
    }

    // Flip all buttons to the default color
    private fun flipAllButtonsToDefault() {
        for (button in buttons) {
            button.setBackgroundColor(resources.getColor(R.color.default_color, theme))
        }
    }

    private fun setupButtonListeners() {
        for (button in buttons) {
            button.setOnClickListener {
                onButtonClicked(button)
            }
        }
    }

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

    // Check if the selected buttons match
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

        // Launch the result activity
        val intent = Intent(this, ResultActivityColorMatch::class.java).apply {
            putExtra("TIME_TAKEN", timeTaken)
            putExtra("ATTEMPTS", attempts)
            putExtra("TIME_CATEGORY", timeCategory)
            putExtra("ATTEMPTS_CATEGORY", attemptsCategory)
        }
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        colorFlipTimer.cancel()
        gameplayTimer.cancel()
    }
}