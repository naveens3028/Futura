package com.upmyranksapp.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.learn.LearnActivity
import com.upmyranksapp.model.Datum
import com.upmyranksapp.model.TopicResponse
import com.upmyranksapp.model.chapter.ChapterResponseData

class SubjectListAdapter(
    val context: Context,
    private val chaptersList: List<ChapterResponseData>,
    val batchId: String
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
        val data = chaptersList[position]
        holder.chapternametxt.text = (chaptersList[position].courseName)
        Log.e("popPos1", position.toString())

        holder.txtIndex.text = "" + (position + 1)

        holder.chapterDetails.text = data.topicMaterialResponses?.size.toString() + " Materials"

        holder.itemView.setOnClickListener {
            val intent = Intent(context, LearnActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("title", chaptersList[position].courseName)
            intent.putExtra("id", chaptersList[position].id)
            intent.putExtra("materials", Gson().toJson(data.topicMaterialResponses))
            intent.putExtra("batchID", batchId)
            Log.e("popPos", position.toString())
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return chaptersList.size
    }
}