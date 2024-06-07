package com.example.cognitiveassessmenttest.ColorMatch

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.GridLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.MainActivity
import com.example.cognitiveassessmenttest.R

class ColorMatchGame : AppCompatActivity() {

    private lateinit var buttons: Array<Button>
    private var colors: MutableList<Int> = mutableListOf()
    private var firstSelected: Button? = null
    private var secondSelected: Button? = null
    private var pairsFound = 0
    private var attempts = 0
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_match_game)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayout)
        buttons = Array(gridLayout.childCount) { Button(this) }
        gridLayout.rowCount = 4
        gridLayout.columnCount = 4

        for (button in buttons) {
            button.layoutParams = GridLayout.LayoutParams().apply {
                width = 0
                height = GridLayout.LayoutParams.WRAP_CONTENT
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
            }
            gridLayout.addView(button)
        }

        initializeColors()
        assignColorsToButtons()
        startTime = System.currentTimeMillis()

        for (button in buttons) {
            button.setOnClickListener {
                onButtonClicked(button)
            }
        }
    }

    private fun initializeColors() {
        val colorList = listOf(
            R.color.red, R.color.blue, R.color.green, R.color.yellow,
            R.color.cyan, R.color.magenta, R.color.orange, R.color.purple
        )
        colors = (colorList + colorList).shuffled().toMutableList()
    }

    private fun assignColorsToButtons() {
        for (i in buttons.indices) {
            buttons[i].tag = colors[i]
            buttons[i].setBackgroundColor(resources.getColor(R.color.default_color, theme))
        }
    }

    private fun onButtonClicked(button: Button) {
        if (button == firstSelected || button == secondSelected) return

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

    private fun checkForMatch() {
        if (firstSelected?.tag == secondSelected?.tag) {
            pairsFound++
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
        val timeTaken = (System.currentTimeMillis() - startTime) / 1000
        val intent = Intent(this, ResultActivityColorMatch::class.java).apply {
            putExtra("TIME_TAKEN", timeTaken)
            putExtra("ATTEMPTS", attempts)
        }
        startActivity(intent)
        finish()
    }
}
