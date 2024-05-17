package com.studygo.studygo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import com.studygo.studygo.adapter.MapelAdapter
import com.studygo.studygo.model.Mapel

class Dashboard : AppCompatActivity() {
    private lateinit var db: DatabaseReference
    private lateinit var storage: FirebaseStorage
    private var user: FirebaseUser? = null

    private lateinit var semangat: TextView
    private lateinit var nama: TextView
    private lateinit var kelas: TextView
    private lateinit var profileBtn: ImageButton
    private lateinit var recyclerMapel: RecyclerView

    private lateinit var adapterMapel: MapelAdapter

    private var listMapel: ArrayList<Mapel> = ArrayList()

    private lateinit var role: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        db = Firebase.database.reference
        storage = Firebase.storage

        semangat = findViewById(R.id.smngt)
        nama = findViewById(R.id.nama)
        kelas = findViewById(R.id.kelas)
        profileBtn = findViewById(R.id.profile)
        recyclerMapel = findViewById(R.id.recycleMapel)

        user = Firebase.auth.currentUser

        profileBtn.setOnClickListener { moveProfile() }

        db.child("users").child(user!!.uid).get().addOnSuccessListener {
            if(it.child("role").value == "siswa") {
                semangat.setText("Semangat Belajar")
            }
            else {
                semangat.setText("Semangat Mengajar")
            }
            nama.setText(it.child("nama").value.toString())
            kelas.setText(it.child("kelas").value.toString())
            role = it.child("role").value.toString()
        }

        db.child("mapel").get().addOnSuccessListener {
            for (i in it.children) {
                listMapel.add(Mapel(i.child("namaMapel").value.toString(), i.child("kelas").value.toString(), i.child("guru").value.toString()))
            }
            adapterMapel = MapelAdapter(listMapel)
            adapterMapel.onClickItem = { mapel -> moveMapel(mapel) }
            recyclerMapel.adapter = adapterMapel
            recyclerMapel.layoutManager = GridLayoutManager(this, 1)
        }
    }

    private fun moveProfile() {
        var intent = Intent(this, ProfilePage::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        if(Firebase.auth.currentUser == null) {
            finish()
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun moveMapel(mapel: Mapel) {
        var intent = Intent(this, MapelPage::class.java)
        intent.putExtra("namaMapel", mapel.namaMapel)
        intent.putExtra("kelas", mapel.kelas)
        intent.putExtra("guru", mapel.guru)
        intent.putExtra("role", role)
        startActivity(intent)
    }
}