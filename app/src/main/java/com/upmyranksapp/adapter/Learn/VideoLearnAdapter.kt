package com.upmyranksapp.adapter.Learn

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.R
import com.upmyranksapp.model.VideoDataModel

class VideoLearnAdapter(
    val context: Context,
    val subjects: ArrayList<VideoDataModel>,
    val videoClicked: OnVideoClicked
) :
    RecyclerView.Adapter<VideoLearnAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val chapternametxt = itemView.findViewById(R.id.chapternametxt) as AppCompatTextView
        val txtIndex = itemView.findViewById(R.id.txtIndex) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_subjectlist, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.chapternametxt.text = subjects[position].name
        holder.txtIndex.text = position.toString()
        holder.chapternametxt.setOnClickListener {
            videoClicked.onVideoSelected(subjects[position].url!!)
        }
    }

    override fun getItemCount(): Int {
        return subjects.size
    }
}

interface OnVideoClicked{
    fun onVideoSelected(url: String)
}