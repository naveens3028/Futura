package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.learn.LearnVideoActivity
import com.trisys.rn.baseapp.model.VideoMaterial
import kotlinx.android.synthetic.main.row_sub_topics_video.view.*

class SubTopicsAdapter(
    val context: Context,
    private val subTopicItems: List<VideoMaterial>
) : RecyclerView.Adapter<SubTopicsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_sub_topics_video, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0)
            holder.itemView.divider.visibility = View.GONE

        holder.itemView.videoName.text = subTopicItems[position].title
        holder.itemView.setOnClickListener {
            val intent = Intent(context, LearnVideoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return subTopicItems.size
    }
}