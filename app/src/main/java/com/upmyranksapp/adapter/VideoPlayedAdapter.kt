package com.upmyranksapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.GlideApp
import com.upmyranksapp.R
import com.upmyranksapp.activity.VideoPlayActivity
import com.upmyranksapp.database.model.VideoPlayedItem
import com.upmyranksapp.helper.exoplayer.ExoUtil
import kotlinx.android.synthetic.main.row_played_video.view.*

class VideoPlayedAdapter(
    val context: Activity,
    private val studyItems: MutableList<VideoPlayedItem>
    , val callback: ActionCallback) : RecyclerView.Adapter<VideoPlayedAdapter.ViewHolder>() {

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
        holder.itemView.videoLayout.setOnClickListener {
            callback.onVideoClickListener(studyItem)
        }
    }

    override fun getItemCount(): Int {
        return studyItems.size
    }

    interface ActionCallback {
        fun onVideoClickListener(videoPlayedItem: VideoPlayedItem)
    }
}