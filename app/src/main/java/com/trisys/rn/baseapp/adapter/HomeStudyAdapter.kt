package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.StudyItem
import kotlinx.android.synthetic.main.row_study.view.*
import kotlinx.android.synthetic.main.row_upcoming_live.view.subject

class HomeStudyAdapter(
    val context: Context,
    private val studyItems: ArrayList<StudyItem>
) : RecyclerView.Adapter<HomeStudyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_study, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studyItem = studyItems[position]
        holder.itemView.subject.text = studyItem.subject
        holder.itemView.lesson.text = studyItem.lesson
        holder.itemView.date.text = studyItem.count
        holder.itemView.backgroundLayout.setBackgroundColor(context.getColor(studyItem.color))
        GlideApp.with(context).load(studyItem.imageID).into(holder.itemView.studyImg)
    }

    override fun getItemCount(): Int {
        return studyItems.size
    }
}