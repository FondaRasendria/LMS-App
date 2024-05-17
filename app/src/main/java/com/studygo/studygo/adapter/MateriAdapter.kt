package com.studygo.studygo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.studygo.studygo.R
import com.studygo.studygo.model.Bab

class MateriAdapter(var items: List<String>): RecyclerView.Adapter<MateriAdapter.ViewHolder>() {
    var onClickItem: ((String) -> Unit)? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var nomorMateri: TextView
        var namaMateri: TextView
        var card: CardView

        init {
            nomorMateri = view.findViewById(R.id.namamateri)
            namaMateri = view.findViewById(R.id.desk)
            card = view.findViewById(R.id.card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.grid_materi, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.namaMateri.setText(items[position])
        holder.nomorMateri.setText("Materi " + (position + 1).toString())
        holder.card.setOnClickListener {
            onClickItem?.invoke(items[position])
        }
    }
}