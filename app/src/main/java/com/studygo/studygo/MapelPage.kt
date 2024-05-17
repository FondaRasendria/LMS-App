package com.studygo.studygo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.studygo.studygo.adapter.BabAdapter
import com.studygo.studygo.model.Bab

class MapelPage : AppCompatActivity() {
    private lateinit var db: DatabaseReference

    private lateinit var namaMapel: String
    private lateinit var kelasMapel: String
    private lateinit var guruMapel: String
    private lateinit var role: String

    private lateinit var mapel: TextView
    private lateinit var kelas: TextView
    private lateinit var guru: TextView
    private lateinit var addBab1: ImageButton
    private lateinit var addBab2: ImageButton
    private lateinit var backBtn: ImageButton
    private lateinit var recyclerBab1: RecyclerView
    private lateinit var recyclerBab2: RecyclerView

    private lateinit var addMapel: BottomSheetDialog

    private var listBabSem1: ArrayList<Bab> = ArrayList()
    private var listBabSem2: ArrayList<Bab> = ArrayList()

    private lateinit var adapterBabSem1: BabAdapter
    private lateinit var adapterBabSem2: BabAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mapel_page)

        namaMapel = intent.getStringExtra("namaMapel")!!
        kelasMapel = intent.getStringExtra("kelas")!!
        guruMapel = intent.getStringExtra("guru")!!
        role = intent.getStringExtra("role")!!

        db = Firebase.database.reference

        mapel = findViewById(R.id.mapel)
        kelas = findViewById(R.id.kelas)
        guru = findViewById(R.id.nama)
        addBab1 = findViewById(R.id.add)
        addBab2 = findViewById(R.id.add2)
        backBtn = findViewById(R.id.back)
        recyclerBab1 = findViewById(R.id.recyclerBab)
        recyclerBab2 = findViewById(R.id.recyclerBab2)

        mapel.setText(namaMapel)
        kelas.setText(kelasMapel)
        guru.setText(guruMapel)

        if(role == "guru") {
            addBab1.visibility = View.VISIBLE
            addBab1.isClickable = true
            addBab2.visibility = View.VISIBLE
            addBab2.isClickable = true

            addBab1.setOnClickListener {
                addMapel = BottomSheetDialog(this)
                var view: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_bab, null)
                addMapel.setContentView(view)
                addMapel.show()
                addbab(view, 1)
            }
            addBab2.setOnClickListener {
                addMapel = BottomSheetDialog(this)
                var view: View = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_bab, null)
                addMapel.setContentView(view)
                addMapel.show()
                addbab(view, 2)
            }
        }

        backBtn.setOnClickListener { finish() }

        loadBab()
    }

    private fun loadBab() {
        listBabSem1.clear()
        listBabSem2.clear()
        db.child("mapel").child(namaMapel.lowercase()).child("bab").get().addOnSuccessListener {
            for(i in it.children) {
                if(i.child("semester").value.toString().toInt() == 1) {
                    listBabSem1.add(Bab(i.child("namaBab").value.toString(), i.child("nomorBab").value.toString().toInt(), i.child("semester").value.toString().toInt()))
                }
                else if(i.child("semester").value.toString().toInt() == 2) {
                    listBabSem2.add(Bab(i.child("namaBab").value.toString(), i.child("nomorBab").value.toString().toInt(), i.child("semester").value.toString().toInt()))
                }
            }
            listBabSem1.sortBy { it.nomorBab }
            adapterBabSem1 = BabAdapter(listBabSem1)
            adapterBabSem1.onClickItem = { bab -> moveBab(bab) }
            recyclerBab1.adapter = adapterBabSem1
            recyclerBab1.layoutManager = LinearLayoutManager(this)

            listBabSem2.sortBy { it.nomorBab }
            adapterBabSem2 = BabAdapter(listBabSem2)
            adapterBabSem2.onClickItem = { bab -> moveBab(bab) }
            recyclerBab2.adapter = adapterBabSem2
            recyclerBab2.layoutManager = LinearLayoutManager(this)
        }
    }

    private fun moveBab(bab: Bab) {
        var intent = Intent(this, BabPage::class.java)
        intent.putExtra("namaMapel", namaMapel)
        intent.putExtra("kelas", kelasMapel)
        intent.putExtra("namaBab", bab.namaBab)
        intent.putExtra("nomorBab", bab.nomorBab)
        intent.putExtra("semester", bab.semester)
        intent.putExtra("role", role)
        startActivity(intent)
    }

    private fun addbab(view: View, semester: Int) {
        var editNamaBab: EditText = view.findViewById(R.id.namabab)
        var editNoBab: EditText = view.findViewById(R.id.no_bab)
        var tambahBab: Button = view.findViewById(R.id.tambahBab)

        tambahBab.setOnClickListener {
            db.child("mapel").child(namaMapel.lowercase()).child("bab").child(editNamaBab.text.toString().lowercase()).setValue(Bab(editNamaBab.text.toString(), editNoBab.text.toString().toInt(), semester))
            addMapel.dismiss()
            loadBab()
        }
    }
}