package com.upmyranksapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.learn.LearnVideoActivity
import com.upmyranksapp.model.VideoMaterial
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.row_sub_topics_video.view.*

class SubTopicsAdapter(
    val context: Context,
    private val subTopicItems: List<VideoMaterial>
) : RecyclerView.Adapter<SubTopicsAdapter.ViewHolder>() {

    lateinit var myPreferences: MyPreferences

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_sub_topics_video, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == 0)
            holder.itemView.divider.visibility = View.GONE

        holder.itemView.videoName.text = subTopicItems[position].title
        holder.itemView.setOnClickListener {
            myPreferences = MyPreferences(context)
            myPreferences.setString(Define.VIDEO_DATA, Gson().toJson(subTopicItems[position]))
            val intent = Intent(context, LearnVideoActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return subTopicItems.size
    }
}