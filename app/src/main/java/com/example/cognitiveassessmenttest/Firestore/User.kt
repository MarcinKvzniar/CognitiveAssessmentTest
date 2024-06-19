package com.example.cognitiveassessmenttest.Firestore

class User(
    val id: String="",
    val name: String="",
    val surname: String="",
    val dob: String="",
    val email: String="",
    val registeredUser: Boolean = false
)