package com.example.cognitiveassessmenttest.firestore

class User(
    val id: String="",
    val name: String="",
    val surname: String="",
    val dob: String="",
    val email: String="",
    val registeredUser: Boolean = false
)