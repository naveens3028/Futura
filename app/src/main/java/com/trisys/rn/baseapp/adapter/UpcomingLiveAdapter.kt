package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.Model.UpcomingLiveItem
import com.trisys.rn.baseapp.R
import kotlinx.android.synthetic.main.row_upcoming_live.view.*

class UpcomingLiveAdapter(
    val context: Context,
    private val upcomingLiveItems: ArrayList<UpcomingLiveItem>
) : RecyclerView.Adapter<UpcomingLiveAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_upcoming_live, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val upcomingLive = upcomingLiveItems[position]
        holder.itemView.subject.text = upcomingLive.subject
        GlideApp.with(context).load(upcomingLive.imageID).into(holder.itemView.upcomingImg)
    }

    override fun getItemCount(): Int {
        return upcomingLiveItems.size
    }
}