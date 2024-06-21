package com.example.cognitiveassessmenttest.firestore

import com.example.cognitiveassessmenttest.RegisterActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStoreClass {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUserFS(activity: RegisterActivity, userInfo: User){

        mFireStore.collection("Users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
            }
    }
}