package com.studygo.studygo.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.studygo.studygo.R

class FileAdapter(var list: List<Uri>): RecyclerView.Adapter<FileAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val gambarFile: ImageView = itemView.findViewById(R.id.gambarFile)
        private val namaFile: TextView = itemView.findViewById(R.id.namaFile)

        fun bind(uri: Uri) {
            gambarFile.setImageURI(uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.file_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}