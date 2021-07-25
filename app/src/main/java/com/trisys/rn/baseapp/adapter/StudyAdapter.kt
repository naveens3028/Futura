package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.Data
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.row_study.view.*
import kotlinx.android.synthetic.main.row_upcoming_live.view.subject

class StudyAdapter(
    val context: Context,
    private val studyItem: List<Data>
) : RecyclerView.Adapter<StudyAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.row_study, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val studyItem = studyItem[position]
        holder.itemView.subject.text = studyItem.subject.courseName
        holder.itemView.lesson.text = studyItem.topicName
        holder.itemView.date.text = Utils.getDateValue(studyItem.sessionDate)
//        holder.itemView.time.progress = 70
        holder.itemView.backgroundLayout.setBackgroundColor(context.getColor(R.color.caribbean_green))
        GlideApp.with(context).load(R.drawable.mathematics).into(holder.itemView.studyImg)
    }

    override fun getItemCount(): Int {
        return 1
    }
}