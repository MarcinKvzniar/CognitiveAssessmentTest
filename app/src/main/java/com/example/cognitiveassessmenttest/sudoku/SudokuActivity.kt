package com.example.cognitiveassessmenttest.sudoku

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.R

class SudokuActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var solveButton: Button
    private val sudokuGenerator = SudokuGenerator()

    private var seconds = 0
    private var running = true
    private lateinit var tvTimer: TextView
    private val handler = Handler(Looper.getMainLooper())

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sudoku)

        gridLayout = findViewById(R.id.sudokuGrid)
        solveButton = findViewById(R.id.solveButton)
        tvTimer = findViewById(R.id.tvTimer)

        runTimer()

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
            editText.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    val value = s.toString()
                    if (value.isNotEmpty()) {
                        val num = value.toInt()
                        val board = Array(4) { IntArray(4) }
                        for (k in 0 until 4) {
                            for (j in 0 until 4) {
                                val et = gridLayout.getChildAt(k * 4 + j) as EditText
                                val square = et.text.toString()
                                board[k][j] = if (square.isEmpty()) 0 else square.toInt()
                            }
                        }
                        if (!sudokuGenerator.isValid(board, i / 4, i % 4, num)) {
                            editText.isFocusable = true
                            editText.isFocusableInTouchMode = true
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
            })
            gridLayout.addView(editText)
        }
        gridLayout.setPadding(10, 10, 10, 10)

        addSampleNumbers()

        solveButton.setOnClickListener {
            running = false
            if (checkSudokuValidity()) {
                Toast.makeText(this, "Sudoku is solved! \n ${tvTimer.text}", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Sudoku is not solved!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addSampleNumbers() {
        val numbers = mutableListOf(1, 2, 3, 4)
        numbers.shuffle()

        val board = Array(4) { IntArray(4) }

        for (i in 0 until 4) {
            val num = numbers[i]

            val editText = gridLayout.getChildAt(i * 4 + i) as EditText
            editText.setText(num.toString())
            editText.isFocusable = false
            editText.isFocusableInTouchMode = false
            board[i][i] = num
        }
    }

    private fun runTimer() {
        val runnable = object : Runnable {
            @SuppressLint("DefaultLocale")
            override fun run() {
                val minutes = seconds % 3600 / 60
                val secs = seconds % 60
                val time = String.format("TIME: %02d:%02d", minutes, secs)
                tvTimer.text = time
                if (running) {
                    seconds++
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    private fun checkSudokuValidity(): Boolean {
        val board = Array(4) { IntArray(4) }
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val editText = gridLayout.getChildAt(i * 4 + j) as EditText
                val value = editText.text.toString()
                board[i][j] = if (value.isEmpty()) 0 else value.toInt()
                if (board[i][j] != 0 && !sudokuGenerator.isValid(board, i, j, board[i][j])) {
                    return false
                }
            }
        }
        return true
    }
}

