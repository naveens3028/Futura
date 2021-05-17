package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.GlideApp
import com.trisys.rn.baseapp.Model.Subjects
import com.trisys.rn.baseapp.R
import com.vpnews24.utils.ImageLoader

class SubjectsAdapter(val context: Context, val subjects: ArrayList<Subjects>) : RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(context: Context,user: Subjects) {
            val imageloader = ImageLoader
            val subjectTxt = itemView.findViewById(R.id.subjecttxt) as TextView
            val subjectImg = itemView.findViewById(R.id.subjectimg) as ImageView
            subjectTxt.text = user.subjects
            GlideApp.with(context).load(user.subjectLogo).into(subjectImg)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_subjects, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(context,subjects[position])
    }

    override fun getItemCount(): Int {
        return subjects.size
    }
}