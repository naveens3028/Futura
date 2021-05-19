package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.Model.CompletedLiveItem
import com.trisys.rn.baseapp.R
import kotlinx.android.synthetic.main.row_completed_live.view.*


class CompletedLiveAdapter(
    val context: Context,
    private val completedLiveItems: ArrayList<CompletedLiveItem>
) : RecyclerView.Adapter<CompletedLiveAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_completed_live, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val completedLive = completedLiveItems[position]
        holder.itemView.subject.text = completedLive.subject
        holder.itemView.iconBackground.setBackgroundColor(context.getColor(completedLive.color))
        GlideApp.with(context).load(completedLive.imageID).into(holder.itemView.icon)
    }

    override fun getItemCount(): Int {
        return completedLiveItems.size
    }
}