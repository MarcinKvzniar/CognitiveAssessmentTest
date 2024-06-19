package com.example.cognitiveassessmenttest

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException

/**
 * This is the login activity for the application.
 * It handles user authentication and navigation to the registration activity.
 */
class LoginActivity : AppCompatActivity() {
    // UI elements and Firebase authentication instance
    private lateinit var btnLogin: Button
    private lateinit var btnGoToRegister: TextView
    private lateinit var inputEmailLog: EditText
    private lateinit var inputPasswordLog: EditText
    private lateinit var showPassword: CheckBox
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and sets up the button listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initViews()

        btnGoToRegister.setOnClickListener {
            startActivity(Intent(this@LoginActivity,
                RegisterActivity::class.java))
        }

        btnLogin.setOnClickListener {
            logInRegisteredUser()
        }
    }

    /**
     * This function initializes the UI elements and Firebase authentication instance.
     */
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

        firebaseAuth = FirebaseAuth.getInstance()
    }

    /**
     * This function validates the login details entered by the user.
     * It checks if the email and password fields are not empty.
     *
     * @return Returns true if the login details are valid, false otherwise.
     */
    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(inputEmailLog.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(getString(R.string.err_msg_enter_email))
                false
            }
            TextUtils.isEmpty(inputPasswordLog.text.toString().trim()) -> {
                showBasicToast(getString(R.string.err_msg_enter_password))
                false
            }
            else -> true
        }
    }

    /**
     * This function logs in the registered user.
     * It validates the login details and authenticates the user with Firebase.
     */
    private fun logInRegisteredUser() {
        if (validateLoginDetails()) {
            val email = inputEmailLog.text.toString().trim()
            val password = inputPasswordLog.text.toString().trim()

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        showBasicToast("Logged in successfully.")
                        goToMainActivity()
                        finish()
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> {
                                getString(R.string.err_msg_user_not_found)
                            }
                            is FirebaseAuthInvalidCredentialsException -> {
                                getString(R.string.err_msg_wrong_password)
                            }
                            else -> {
                                getString(R.string.err_msg_unknown_error)
                            }
                        }
                        showBasicToast(errorMessage)
                    }
                }
        }
    }

    /**
     * This function navigates to the main activity.
     */
    private fun goToMainActivity() {
        val uid = FirebaseAuth.getInstance().currentUser?.email.toString()

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("uID", uid)
        startActivity(intent)
    }

    /**
     * This function displays a basic toast message.
     *
     * @param message The message to be displayed in the toast.
     */
    private fun showBasicToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}