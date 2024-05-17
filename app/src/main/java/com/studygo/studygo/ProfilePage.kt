package com.studygo.studygo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class ProfilePage : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private var user: FirebaseUser? = null

    private lateinit var nama: TextView
    private lateinit var kelas: TextView
    private lateinit var logout: Button
    private lateinit var back: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        db = Firebase.database.reference
        user = Firebase.auth.currentUser

        nama = findViewById(R.id.nama)
        kelas = findViewById(R.id.kelas)
        logout = findViewById(R.id.logout)
        back = findViewById(R.id.back)

        logout.setOnClickListener { SignOut() }
        back.setOnClickListener { finish() }

        db.child("users").child(user!!.uid).get().addOnSuccessListener {
            nama.setText(it.child("nama").value.toString())
            kelas.setText(it.child("kelas").value.toString())
        }
    }

    private fun SignOut() {
        Firebase.auth.signOut()
        finish()
    }
}