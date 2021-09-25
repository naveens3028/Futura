package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.database.model.VideoPlayedItem
import kotlinx.android.synthetic.main.row_played_video.view.*

class VideoPlayedAdapter(
    val context: Context,
    private val studyItems: MutableList<VideoPlayedItem>
) : RecyclerView.Adapter<VideoPlayedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_played_video, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studyItem = studyItems[position]
        holder.itemView.videoTitle.text = studyItem.videoTitle
        GlideApp.with(context).load(studyItem.logoImg).into(holder.itemView.videoImgs)
    }

    override fun getItemCount(): Int {
        return studyItems.size
    }
}