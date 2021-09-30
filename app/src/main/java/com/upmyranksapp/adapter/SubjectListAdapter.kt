package com.upmyranksapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.R
import com.upmyranksapp.learn.LearnActivity
import com.upmyranksapp.model.Datum
import com.upmyranksapp.model.TopicResponse

class SubjectListAdapter(
    val context: Context,
    private val chaptersList: ArrayList<Datum>,
    val batchId: String,
    private val topicList : ArrayList<TopicResponse>
) :
    RecyclerView.Adapter<SubjectListAdapter.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapternametxt = itemView.findViewById(R.id.chapternametxt) as TextView
        val txtIndex = itemView.findViewById(R.id.txtIndex) as TextView
        val chapterDetails = itemView.findViewById(R.id.chapterDetails) as AppCompatTextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_subjectlist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapternametxt.text = (chaptersList[position].courseName)
        holder.txtIndex.text = "" + (position + 1)

        holder.chapterDetails.text = getContent(chaptersList[position].id).toString() +" Materials"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("title", chaptersList[position].courseName)
            intent.putExtra("id", chaptersList[position].id)
            intent.putExtra("batchID", batchId)
            context.startActivity(intent)
        }
    }

    private fun getContent(courseId: String?): Int {
        var data = 0
        topicList.filter {
            it[0].topic.parentId.equals(courseId)
        }.map { data = if (!it[0].materialList.isNullOrEmpty()){
            it[0].materialList!!.size
        }else{
            0
        }
        }
        return data
    }

    override fun getItemCount(): Int {
        return chaptersList.size
    }
}