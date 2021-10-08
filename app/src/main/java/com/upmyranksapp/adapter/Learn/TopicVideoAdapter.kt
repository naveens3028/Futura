package com.upmyranksapp.adapter.Learn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.R
import com.upmyranksapp.model.chapter.TopicMaterialResponse


class TopicVideoAdapter(
    val context: Context,
    val subjects: List<TopicMaterialResponse>?
) :
    RecyclerView.Adapter<TopicVideoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt_topic_header = itemView.findViewById(R.id.txt_topic_header) as AppCompatTextView
        val img_topic_header = itemView.findViewById(R.id.img_topic_header) as AppCompatImageView
        val videoRecycler = itemView.findViewById(R.id.recyclerTopicSubItems) as RecyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_topic_items, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_topic_header.text = subjects!![position].topic!!.courseName
         val adapter = LearnTopicHeaderAdapter(context, subjects!![position].materialList)
        holder.videoRecycler.adapter = adapter
        holder.img_topic_header.setOnClickListener {
            if (holder.videoRecycler.visibility == View.VISIBLE) {
                holder.videoRecycler.visibility = View.GONE
            }else{
                holder.videoRecycler.visibility = View.VISIBLE
            }
        }

    }

    override fun getItemCount(): Int {
        return subjects?.size!!
    }
}