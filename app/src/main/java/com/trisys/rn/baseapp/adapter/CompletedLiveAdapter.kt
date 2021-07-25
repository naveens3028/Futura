package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.model.CompletedLiveItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.LiveResponse
import com.trisys.rn.baseapp.model.onBoarding.CompletedSession
import kotlinx.android.synthetic.main.row_completed_live.view.*


class CompletedLiveAdapter(
    val context: Context,
    private val completedLiveItems: ArrayList<CompletedLiveItem>,
    private val complLive : ArrayList<CompletedSession>,
    private val isCompletedLive: Boolean
) : RecyclerView.Adapter<CompletedLiveAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_completed_live, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isCompletedLive) {
            val completedLive = complLive[position]
            holder.itemView.subject.text = completedLive.courseName
            holder.itemView.backgroundColor.setBackgroundColor(context.getColor(R.color.mikado_yellow))
            holder.itemView.lesson.text = completedLive.description
        }else{
            val completedLive = completedLiveItems[position]
            holder.itemView.subject.text = completedLive.subject
            holder.itemView.backgroundColor.setBackgroundColor(context.getColor(completedLive.color))
            holder.itemView.lesson.text = completedLive.lesson
        }
    }

    override fun getItemCount(): Int {
        if (isCompletedLive) {
            return complLive.size
        }else{
            return completedLiveItems.size
        }
    }
}