package com.studygo.studygo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.initialize

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null

    private lateinit var toRegister: TextView
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth

        toRegister = findViewById(R.id.to_regis)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        btnLogin = findViewById(R.id.btnlog)

        toRegister.setOnClickListener { moveRegister() }
        btnLogin.setOnClickListener {SignIn(email.text.toString(), password.text.toString())}
    }

    private fun moveRegister() {
        var intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        user = auth.currentUser
        if(user != null) {
            finish()
            var intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
        }
    }

    private fun SignIn(email: String, password: String) {
        if(this.email.text.isEmpty()) {
            this.email.error = "Email harus diisi"
            return
        }
        if(this.password.text.isEmpty()) {
            this.password.error = "Password harus diisi"
            return
        }
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Toast.makeText(applicationContext, "Welcome", Toast.LENGTH_SHORT).show()
                    user = auth.currentUser
                    finish()
                    var intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(applicationContext, "Sign In Failed", Toast.LENGTH_SHORT).show()
                    Log.e("ERROR: ", task.exception.toString())
                    //Reload
                }
            }
    }
}