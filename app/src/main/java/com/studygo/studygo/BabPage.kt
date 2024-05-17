package com.studygo.studygo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.studygo.studygo.adapter.FileAdapter
import com.studygo.studygo.adapter.MateriAdapter

class BabPage : AppCompatActivity() {
    private lateinit var addButton: ImageButton
    private lateinit var backButton: ImageButton
    private lateinit var namaBabText: TextView
    private lateinit var namaMapelText: TextView
    private lateinit var kelasMapelText: TextView
    private lateinit var nomorBabText: TextView
    private lateinit var recyclerMateri: RecyclerView
    private lateinit var editJudulMateri: EditText

    private var listMateri: ArrayList<String> = ArrayList()
    private lateinit var adapterMateri: MateriAdapter

    private lateinit var namaMapel: String
    private lateinit var kelasMapel: String
    private lateinit var namaBab: String
    private lateinit var nomorBab: String
    private lateinit var semester: String
    private lateinit var role: String

    private lateinit var addBab: BottomSheetDialog

    private var storage: StorageReference = Firebase.storage.reference
    private var db: DatabaseReference = Firebase.database.reference
    private var selectedFile = mutableListOf<Uri>()
    private lateinit var fileAdapter: FileAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bab_page)

        addButton = findViewById(R.id.add)
        backButton = findViewById(R.id.back)
        namaBabText = findViewById(R.id.namabab)
        namaMapelText = findViewById(R.id.mapel)
        kelasMapelText = findViewById(R.id.kelas)
        nomorBabText = findViewById(R.id.no_bab)
        recyclerMateri = findViewById(R.id.recyclerMateri)

        namaMapel = intent.getStringExtra("namaMapel")!!
        kelasMapel = intent.getStringExtra("kelas")!!
        namaBab = intent.getStringExtra("namaBab")!!
        nomorBab = intent.getIntExtra("nomorBab", 0).toString()
        semester = intent.getIntExtra("semester", 0).toString()
        role = intent.getStringExtra("role")!!

        namaBabText.setText(namaBab)
        namaMapelText.setText(namaMapel)
        kelasMapelText.setText("Kelas $kelasMapel")
        nomorBabText.setText("BAB $nomorBab")

        if(role == "guru") {
            addButton.visibility = View.VISIBLE
            addButton.isClickable = true

            addButton.setOnClickListener {
                addBab = BottomSheetDialog(this)
                var view: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_materi, null)
                addBab.setContentView(view)
                addBab.show()
                addMateri(view)
            }
        }

        backButton.setOnClickListener { finish() }

        loadMateri()
    }

    private fun loadMateri() {
        listMateri.clear()
        db.child("mapel").child(namaMapel.lowercase()).child("bab").child(namaBab.lowercase()).child("materi").get().addOnSuccessListener {
            for(i in it.children) {
                listMateri.add(i.child("judulMateri").value.toString())
            }
            adapterMateri = MateriAdapter(listMateri)
            adapterMateri.onClickItem = { judulMateri ->  moveMateri(judulMateri)}
            recyclerMateri.adapter = adapterMateri
            recyclerMateri.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun moveMateri(judulMateri: String) {
        val intent = Intent(this, MateriPage::class.java)
        intent.putExtra("judulMateri", judulMateri)
        intent.putExtra("namaMapel", namaMapel)
        intent.putExtra("nomorBab", nomorBab)
        intent.putExtra("namaBab", namaBab)
        startActivity(intent)
    }

    private fun addMateri(view: View) {
        editJudulMateri = view.findViewById(R.id.namamateri)
        var editDeskripsiMateri = view.findViewById<EditText>(R.id.desk)
        var uploadButton = view.findViewById<Button>(R.id.upload)
        var simpanButton = view.findViewById<Button>(R.id.simpan)
        var recycler = view.findViewById<RecyclerView>(R.id.recyclerFile)

        fileAdapter = FileAdapter(selectedFile)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = fileAdapter

        uploadButton.setOnClickListener{
            openFileSelector()
        }
        simpanButton.setOnClickListener{
            if(selectedFile.isNotEmpty()) {
                uploadToFirebase()
                db.child("mapel").child(namaMapel.lowercase()).child("bab").child(namaBab.lowercase()).child("materi").child(editJudulMateri.text.toString().lowercase()).child("judulMateri").setValue(editJudulMateri.text.toString())
                db.child("mapel").child(namaMapel.lowercase()).child("bab").child(namaBab.lowercase()).child("materi").child(editJudulMateri.text.toString().lowercase()).child("deskripsi").setValue(editDeskripsiMateri.text.toString())
            } else {
                Toast.makeText(applicationContext, "No file selected", Toast.LENGTH_SHORT).show()
            }
            addBab.dismiss()
            loadMateri()
        }
    }

    private fun openFileSelector() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(Intent.createChooser(intent, "Select Files"), 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 100 && resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                if(intent.clipData != null) {
                    val count = intent.clipData?.itemCount ?:0
                    for(i in 0 until count) {
                        intent.clipData?.getItemAt(i)?.uri?.let { uri ->
                            selectedFile.add(uri)
                        }
                    }
                } else {
                    intent.data?.let { uri -> {
                        selectedFile.add(uri)
                    } }
                }
                fileAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun uploadToFirebase() {
        for(uri in selectedFile) {
            val fileRef = storage.child("upload/${System.currentTimeMillis()}")
            fileRef.putFile(uri)
                .addOnSuccessListener {taskSnapshot ->
                    Toast.makeText(applicationContext, "File Upload Successfully", Toast.LENGTH_SHORT).show()
                    val filePath = taskSnapshot.metadata?.path
                    val fileName = taskSnapshot.metadata?.name
                    db.child("mapel").child(namaMapel.lowercase()).child("bab").child(namaBab.lowercase()).child("materi").child(editJudulMateri.text.toString().lowercase()).child("files").child(fileName?:"").setValue(filePath?:"")
                }
                .addOnFailureListener {
                    Toast.makeText(applicationContext, "File Upload Failure", Toast.LENGTH_SHORT).show()
                }
        }
    }
}