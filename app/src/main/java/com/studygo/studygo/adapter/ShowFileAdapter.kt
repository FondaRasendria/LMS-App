package com.studygo.studygo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.studygo.studygo.R

class ShowFileAdapter(var items: List<String>): RecyclerView.Adapter<ShowFileAdapter.ViewHolder>() {
    var onClickitem: ((String) -> Unit)? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var namaFile: TextView
        var card: CardView

        init {
            namaFile = view.findViewById(R.id.namaFile)
            card = view.findViewById(R.id.card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.grid_file, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaFile.setText(items[position])
        holder.card.setOnClickListener {
            onClickitem?.invoke(items[position])
        }
    }
}