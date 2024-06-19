package com.example.cognitiveassessmenttest

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.sudoku.SudokuActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var btnLogin: Button
    private lateinit var btnGoToRegister: TextView
    private lateinit var inputEmailLog: EditText
    private lateinit var inputPasswordLog: EditText
    private lateinit var showPassword: CheckBox

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        btnLogin.setOnClickListener {
            val intent = Intent(this, SudokuActivity::class.java)
            startActivity(intent)
            // TODO implement login logic
        }

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }
    }

    private fun initViews() {
        btnLogin = findViewById(R.id.btnLogin)
        btnGoToRegister = findViewById(R.id.btnGoToRegister)
        inputEmailLog = findViewById(R.id.inputEmailLog)
        inputPasswordLog = findViewById(R.id.inputPasswordLog)
        showPassword = findViewById(R.id.showPassword)

        showPassword.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                inputPasswordLog
                    .transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                inputPasswordLog
                    .transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
    }
}