package com.example.cognitiveassessmenttest

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cognitiveassessmenttest.firestore.FireStoreClass
import com.example.cognitiveassessmenttest.firestore.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat

/**
 * This is the registration activity for the application.
 * It handles user registration and navigation to the login activity.
 */
class RegisterActivity : AppCompatActivity() {

    // UI elements and Firebase authentication instance
    private lateinit var btnRegister: Button
    private lateinit var btnGoToLogin: TextView
    private lateinit var inputUsernameReg: EditText
    private lateinit var inputDateOfBirthReg: EditText
    private lateinit var inputEmailReg: EditText
    private lateinit var inputPasswordReg: EditText
    private lateinit var inputRepPasswordReg: EditText
    private lateinit var firebaseAuth: FirebaseAuth

    /**
     * This function is called when the activity is starting.
     * It initializes the activity, sets the content view, and sets up the button listeners.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        initViews()

        btnGoToLogin.setOnClickListener {
            startActivity(Intent(this@RegisterActivity,
                LoginActivity::class.java))
        }

        btnRegister.setOnClickListener {
            if (validateRegisterDetails()) {
                registerUser()
            }
        }
    }

    /**
     * This function initializes the UI elements and Firebase authentication instance.
     */
    private fun initViews() {
        btnRegister = findViewById(R.id.btnRegister)
        btnGoToLogin = findViewById(R.id.btnGoToLogin)
        inputUsernameReg = findViewById(R.id.inputUsername)
        inputDateOfBirthReg = findViewById(R.id.inputDateOfBirthReg)
        inputEmailReg = findViewById(R.id.inputEmailReg)
        inputPasswordReg = findViewById(R.id.inputPasswordReg)
        inputRepPasswordReg = findViewById(R.id.inputRepeatPasswordReg)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    /**
     * This function validates the registration details entered by the user.
     * It checks if the username, date of birth, email, and password fields are not empty.
     * It also checks if the password contains special characters and if the repeated password matches the original password.
     *
     * @return Returns true if the registration details are valid, false otherwise.
     */
    private fun validateRegisterDetails(): Boolean {
        val specialChars = "!@#$%^&*-_+(){}/[]|".toCharArray()

        return when {
            TextUtils.isEmpty(inputUsernameReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_name))
                false
            }
            TextUtils.isEmpty(inputDateOfBirthReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_date_of_birth))
                false
            }
            !isDateValid(inputDateOfBirthReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_date_of_birth_format))
                false
            }
            TextUtils.isEmpty(inputEmailReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_email))
                false
            }
            TextUtils.isEmpty(inputPasswordReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_password))
                false
            }
            TextUtils.isEmpty(inputRepPasswordReg.text.toString().trim { it <= ' ' }) -> {
                showBasicToast(resources.getString(R.string.err_msg_enter_reppassword))
                false
            }
            inputPasswordReg.text.toString().trim { it <= ' ' }.length < 8 -> {
                showBasicToast(resources.getString(R.string.err_msg_password_length))
                false
            }
            !(inputPasswordReg.text.toString().trim { it <= ' ' }.any {
                    char -> char in specialChars}) -> {
                showBasicToast(resources.getString(R.string.err_msg_password_special_chars))
                false
            }
            inputPasswordReg.text.toString().trim { it <= ' ' } !=
                    inputRepPasswordReg.text.toString().trim { it <= ' ' } -> {
                showBasicToast(resources.getString(R.string.err_msg_password_mismatch))
                false
            }
            else -> true
        }
    }

    /**
     * This function registers the user.
     * It validates the registration details and creates a new user with Firebase.
     */
    private fun registerUser() {
        val email = inputEmailReg.text.toString().trim()
        val password = inputPasswordReg.text.toString().trim()
        val username = inputUsernameReg.text.toString().trim()
        val dob = inputDateOfBirthReg.text.toString().trim()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result?.user!!
                    showBasicToast("You are registered successfully." +
                            " Your user id is ${firebaseUser.uid}")

                    val user = User(firebaseUser.uid, username, dob, email, true)

                    val db = Firebase.firestore

                    val userData = hashMapOf(
                        "username" to username,
                        "dob" to dob,
                        "email" to email
                    )

                    db.collection("users")
                        .document(firebaseUser.uid)
                        .set(userData)
                        .addOnSuccessListener {
                            FireStoreClass().registerUserFS(this@RegisterActivity, user)
                        }
                        .addOnFailureListener { e ->
                            showBasicToast(e.message.toString())
                        }

                    startActivity(
                        Intent(this@RegisterActivity,
                            LoginActivity::class.java)
                    )
                    finish()
                } else {
                    showBasicToast(task.exception?.message.toString())
                }
            }
    }

    /**
     * This function checks if a given date is valid.
     *
     * @param date The date to be checked.
     * @return Returns true if the date is valid, false otherwise.
     */
    @SuppressLint("SimpleDateFormat")
    private fun isDateValid(date: String): Boolean {
        val format = SimpleDateFormat("yyyy-MM-dd")
        format.isLenient = false
        return try {
            format.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * This function displays a basic toast message.
     *
     * @param message The message to be displayed in the toast.
     */
    private fun showBasicToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * This function is called when the user registration is successful.
     */
    fun userRegistrationSuccess() {
        Toast.makeText(this@RegisterActivity, getString(R.string.register_success),
            Toast.LENGTH_LONG).show()
    }
}