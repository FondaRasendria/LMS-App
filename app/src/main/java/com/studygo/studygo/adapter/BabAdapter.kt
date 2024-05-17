package com.studygo.studygo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.studygo.studygo.R
import com.studygo.studygo.model.Bab
import com.studygo.studygo.model.Mapel

class BabAdapter(var items: List<Bab>): RecyclerView.Adapter<BabAdapter.ViewHolder>() {
    var onClickItem: ((Bab) -> Unit)? = null

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        var namaBab: TextView
        var noBab: TextView
        var card: CardView

        init {
            namaBab = view.findViewById(R.id.namabab)
            noBab = view.findViewById(R.id.no_bab)
            card = view.findViewById(R.id.card)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.grid_bab, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var bab = items[position]
        holder.namaBab.setText(bab.namaBab)
        holder.noBab.setText("Bab " + bab.nomorBab)
        holder.card.setOnClickListener {
            onClickItem?.invoke(bab)
        }
    }
}