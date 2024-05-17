package com.studygo.studygo.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.studygo.studygo.MapelPage
import com.studygo.studygo.R
import com.studygo.studygo.model.Mapel

class MapelAdapter(var items: List<Mapel>): RecyclerView.Adapter<MapelAdapter.ViewHolder>() {
    var onClickItem: ((Mapel) -> Unit)? = null
    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var mapel: TextView
        var kelas: TextView
        var card: CardView

        init {
            mapel = view.findViewById(R.id.mapel)
            kelas = view.findViewById(R.id.kelas)
            card = view.findViewById(R.id.card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.grid_mapel, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mapel = items[position]
        holder.mapel.setText(mapel.namaMapel)
        holder.kelas.setText(mapel.kelas)
        holder.card.setOnClickListener {
            onClickItem?.invoke(mapel)
        }
    }
}