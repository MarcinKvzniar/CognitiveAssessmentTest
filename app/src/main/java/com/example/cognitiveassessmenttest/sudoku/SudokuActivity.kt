package com.example.cognitiveassessmenttest.sudoku

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R

class SudokuActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var solveButton: Button
    private val sudokuGenerator = SudokuGenerator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)

        gridLayout = findViewById(R.id.sudokuGrid)
        solveButton = findViewById(R.id.solveButton)

        for (i in 0 until 16) {
            val editText = EditText(this)
            editText.layoutParams = GridLayout.LayoutParams().apply {
                width = 200
                height = 200
                rowSpec = GridLayout.spec(i / 4, 1f)
                columnSpec = GridLayout.spec(i % 4, 1f)
                setMargins(2, 2, 2, 2)
                setGravity(Gravity.FILL)
            }
            editText.setBackgroundResource(R.drawable.edit_text_border)
            editText.gravity = Gravity.CENTER
            editText.setTextColor(Color.BLACK)
            editText.textSize = 18f
            gridLayout.addView(editText)
        }
        gridLayout.setPadding(10, 10, 10, 10)

        addSampleNumbers()

        solveButton.setOnClickListener {
            val board = Array(4) { IntArray(4) }
            var allInputsValid = true
            for (i in 0 until 4) {
                for (j in 0 until 4) {
                    val editText = gridLayout.getChildAt(i * 4 + j) as EditText
                    val value = editText.text.toString()
                    board[i][j] = if (value.isEmpty()) 0 else value.toInt()
                    if (board[i][j] != 0 && !sudokuGenerator.isValid(board, i, j, board[i][j])) {
                        allInputsValid = false
                        break
                    }
                }
                if (!allInputsValid) {
                    break
                }
            }
            if (allInputsValid) {
                Toast.makeText(this, "Sudoku is valid", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sudoku is not valid", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addSampleNumbers() {
        val sampleNumbers = arrayOf(1, 2, 3, 4)
        for (i in sampleNumbers.indices) {
            val editText = gridLayout.getChildAt(i * 5) as EditText
            editText.setText(sampleNumbers[i].toString())
        }
    }
}

