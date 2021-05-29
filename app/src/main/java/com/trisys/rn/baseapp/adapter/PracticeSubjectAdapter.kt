package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.PracticeSubjects
import kotlinx.android.synthetic.main.practice_layout.view.*

class PracticeSubjectAdapter(
    val context: Context,
    private val scheduledTestItems: ArrayList<PracticeSubjects>
) : RecyclerView.Adapter<PracticeSubjectAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.practice_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledTestItems[position]
        holder.itemView.pracsubjtxt.text = scheduledTest.subjectPractice
        holder.itemView.markspractxt.text = scheduledTest.subjectPracticeMarks

        when (holder.itemView.pracsubjtxt.text){

            "Physics" ->{
                holder.itemView.cardpracsubject.setCardBackgroundColor(context.getColor(R.color.bio_pink))
            }
            "Chemistry" ->{
                holder.itemView.cardpracsubject.setCardBackgroundColor(context.getColor(R.color.purple_300))
            }
            "Biology" ->{
                holder.itemView.cardpracsubject.setCardBackgroundColor(context.getColor(R.color.mikado_yellow))
            }

        }
    }

    override fun getItemCount(): Int {
        return scheduledTestItems.size
    }
}