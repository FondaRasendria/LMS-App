package com.example.studygo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.studygo.studygo.R

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var email = ""
    private var password = ""
    private var user: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        user = auth.currentUser
        if(user != null) {
            //Pindah?
        }
    }

    private fun SignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Toast.makeText(applicationContext, "Register Successful", Toast.LENGTH_SHORT).show()
                    user = auth.currentUser
                    //Pindah?
                } else {
                    Toast.makeText(applicationContext, "Register Failed", Toast.LENGTH_LONG).show()
                    Log.e("ERROR: ", task.exception.toString())
                    //Reload
                }
            }
    }

    private fun SignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()
                    user = auth.currentUser
                    //Pindah
                } else {
                    Toast.makeText(applicationContext, "Sign In Failed", Toast.LENGTH_SHORT).show()
                    Log.e("ERROR: ", task.exception.toString())
                    //Reload
                }
            }
    }
}