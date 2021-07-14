package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.learn.LearnActivity
import com.trisys.rn.baseapp.model.Datum

class SubjectListAdapter(
    val context: Context,
    private val chaptersList: ArrayList<Datum>,
    val batchId: String
) :
    RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapternametxt = itemView.findViewById(R.id.chapternametxt) as TextView
        val txtIndex = itemView.findViewById(R.id.txtIndex) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_subjectlist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapternametxt.text = (chaptersList[position].courseName)
        holder.txtIndex.text = "" + (position + 1)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            intent.putExtra("title", chaptersList[position].courseName)
            intent.putExtra("id", chaptersList[position].id)
            intent.putExtra("batchID", batchId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chaptersList.size
    }
}