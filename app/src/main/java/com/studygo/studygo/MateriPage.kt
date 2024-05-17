package com.studygo.studygo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.studygo.studygo.adapter.ShowFileAdapter
import java.io.File

class MateriPage : AppCompatActivity() {
    private lateinit var judulMateri: String
    private lateinit var deskripsi: String
    private lateinit var namaMapel: String
    private lateinit var nomorBab: String
    private lateinit var namaBab: String

    private lateinit var backBtn: ImageButton
    private lateinit var materiText: TextView
    private lateinit var nomorBabText: TextView
    private lateinit var namaBabText: TextView
    private lateinit var judul: TextView
    private lateinit var deskripsiText: TextView
    private lateinit var recycler: RecyclerView

    private lateinit var adapterFile: ShowFileAdapter
    private var listFile: ArrayList<String> = ArrayList()

    private var db: DatabaseReference = Firebase.database.reference
    private var storage: StorageReference = Firebase.storage.reference

    private val STORAGE_PERMISSION_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_materi_page)

        judulMateri = intent.getStringExtra("judulMateri")!!
        namaMapel = intent.getStringExtra("namaMapel")!!
        nomorBab = intent.getStringExtra("nomorBab")!!
        namaBab = intent.getStringExtra("namaBab")!!

        backBtn = findViewById(R.id.back)
        materiText = findViewById(R.id.mapel)
        nomorBabText = findViewById(R.id.nomorbab)
        namaBabText = findViewById(R.id.namabab)
        judul = findViewById(R.id.judul)
        deskripsiText = findViewById(R.id.deskripsi)
        recycler = findViewById(R.id.recyclerShowFile)

        materiText.setText(namaMapel)
        nomorBabText.setText("Bab " + nomorBab)
        namaBabText.setText(namaBab)

        judul.setText(judulMateri)

        db.child("mapel").child(namaMapel.lowercase()).child("bab").child(namaBab.lowercase()).child("materi").child(judulMateri.lowercase()).get().addOnSuccessListener {
            for (i in it.children) {
                if(i.key.toString() == "deskripsi") {
                    deskripsi = i.value.toString()
                    deskripsiText.setText(deskripsi)
                }
                if(i.key.toString() == "files") {
                    for(j in i.children) {
                        listFile.add(j.value.toString())
                    }
                }
            }
            adapterFile = ShowFileAdapter(listFile)
            adapterFile.onClickitem = {
                val fileRef = storage.child(it)
                val localFile = File.createTempFile("temp", "jpg")

                fileRef.downloadUrl.addOnSuccessListener {
                    val intent: Intent = Intent(Intent.ACTION_VIEW).setData(it)
                    startActivity(intent)
                    Toast.makeText(applicationContext, "Downloaded", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(applicationContext, "Failed to download", Toast.LENGTH_SHORT).show()
                }
            }
            recycler.layoutManager = GridLayoutManager(this, 2)
            recycler.adapter = adapterFile
        }

        backBtn.setOnClickListener { finish() }
    }
}