package com.studygo.studygo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser? = null
    private var database: DatabaseReference? = null

    private lateinit var registerButton: Button
    private lateinit var emailText: EditText
    private lateinit var passwordText: EditText
    private lateinit var namaText: EditText
    private lateinit var kelasText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = Firebase.auth
        database = Firebase.database.reference

        registerButton = findViewById(R.id.btnregis)
        emailText = findViewById(R.id.email)
        passwordText = findViewById(R.id.password)
        namaText = findViewById(R.id.nama)
        kelasText = findViewById(R.id.kelas)

        registerButton.setOnClickListener{SignUp(emailText.text.toString(), passwordText.text.toString(), namaText.text.toString(), kelasText.text.toString())}
    }

    private fun SignUp(email: String, password: String, nama: String, kelas: String) {
        if(emailText.text.isEmpty()) {
            emailText.error = "Email harus diisi"
            return
        }
        if(passwordText.text.isEmpty() && passwordText.text.length < 8) {
            passwordText.error = "Password harus diisi dan minimal 8 huruf"
            return
        }
        if(namaText.text.isEmpty()) {
            namaText.error = "Nama harus diisi"
            return
        }
        if(kelasText.text.isEmpty()) {
            kelasText.error = "Kelas harus diisi"
            return
        }
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if(task.isSuccessful) {
                    Toast.makeText(applicationContext, "Register Successful", Toast.LENGTH_SHORT).show()
                    user = auth.currentUser
                    database?.child("users")?.child(user!!.uid)?.child("email")?.setValue(email)
                    database?.child("users")?.child(user!!.uid)?.child("nama")?.setValue(nama)
                    database?.child("users")?.child(user!!.uid)?.child("kelas")?.setValue(kelas)
                    database?.child("users")?.child(user!!.uid)?.child("role")?.setValue("siswa")
                    finish()
                } else {
                    Toast.makeText(applicationContext, "Register Failed", Toast.LENGTH_SHORT).show()
                    Log.e("ERROR: ", task.exception.toString())
                }
            }
    }
}