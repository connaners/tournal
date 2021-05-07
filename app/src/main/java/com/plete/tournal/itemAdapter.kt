package com.plete.tournal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_row.view.*

class itemAdapter(val context: Context, val items: ArrayList<dbModel>): RecyclerView.Adapter<itemAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val llmain = view.llmain
        val tvId = view.itemTvId
        val tvDesc = view.itemTvDesc
        val tvDate = view.itemTvDate
        val tvLoc = view.itemTvLoc
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_row, parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = items.get(position)
        holder.tvId.text = items.id.toString()
        holder.tvDesc.text = items.desc
        holder.tvDate.text = items.date
        holder.tvLoc.text = items.loc

        if (position % 2 == 0){
            holder.llmain.setBackgroundColor(ContextCompat.getColor(context, R.color.blue))
        } else {
            holder.llmain.setBackgroundColor(ContextCompat.getColor(context, R.color.pink))
        }
    }
}