package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.onBoarding.CompletedSession
import kotlinx.android.synthetic.main.row_completed_live.view.*


class CompletedLiveAdapter(
    val context: Context,
    private val completedLive : ArrayList<CompletedSession>,
) : RecyclerView.Adapter<CompletedLiveAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_completed_live, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val completedLive = completedLive[position]
        holder.itemView.subject.text = completedLive.courseName
        holder.itemView.backgroundColor.setBackgroundColor(context.getColor(R.color.mikado_yellow))
        holder.itemView.lesson.text = completedLive.description
    }

    override fun getItemCount(): Int {
            return completedLive.size
    }
}