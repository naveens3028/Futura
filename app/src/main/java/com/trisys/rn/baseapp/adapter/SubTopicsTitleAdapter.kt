package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.learn.TopicClickListener
import com.trisys.rn.baseapp.model.TopicResponseItem
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.row_sub_topics_title.view.*

class SubTopicsTitleAdapter(
    val context: Context,
    private val subTopicTitleItems: ArrayList<TopicResponseItem>,
    private var topicClickListener: TopicClickListener
) : RecyclerView.Adapter<SubTopicsTitleAdapter.ViewHolder>() {

    private var previousPosition = -1
    var currentPosition = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_sub_topics_title, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val subTopicTitle = subTopicTitleItems[position]

        holder.itemView.subject.text = subTopicTitle.topic.courseName

        if (currentPosition == position) {
            holder.itemView.selected.visibility = View.VISIBLE
        } else {
            holder.itemView.selected.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            topicClickListener.onTopicSelected(subTopicTitle.materialList)
            previousPosition = currentPosition
            currentPosition = position
            if (previousPosition != -1) notifyItemChanged(previousPosition)
            notifyItemChanged(currentPosition)
        }
    }

    override fun getItemCount(): Int {
        return subTopicTitleItems.size
    }
}