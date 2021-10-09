package com.upmyranksapp.adapter.Learn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.R
import com.upmyranksapp.model.VideoMaterial

class LearnTopicHeaderAdapter(
    val context: Context,
    val subjects: List<VideoMaterial>?,
    val listener: VideoClickListener
) :
    RecyclerView.Adapter<LearnTopicHeaderAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapternametxt = itemView.findViewById(R.id.videoName) as AppCompatTextView
        val subTopicTitleCard = itemView.findViewById(R.id.subTopicTitleCard) as LinearLayoutCompat
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item_sub_topic, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapternametxt.text = subjects?.get(position)?.courseName
        holder.subTopicTitleCard.setOnClickListener {
            listener.onVideoSelected(subjects!![position])
        }
    }

    override fun getItemCount(): Int {
        return subjects?.size!!
    }
}

interface VideoClickListener{
    fun onVideoSelected(videoMaterial: VideoMaterial)
}