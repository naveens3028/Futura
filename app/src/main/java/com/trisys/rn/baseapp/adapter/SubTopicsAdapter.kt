package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.Model.SubTopicItem
import com.trisys.rn.baseapp.R
import kotlinx.android.synthetic.main.row_sub_topics_video.view.*

class SubTopicsAdapter(
    val context: Context,
    private val subTopicItems: ArrayList<SubTopicItem>
) : RecyclerView.Adapter<SubTopicsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_sub_topics_video, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.adapterPosition == 0)
            holder.itemView.divider.visibility = View.GONE

    }

    override fun getItemCount(): Int {
        return 5
    }
}