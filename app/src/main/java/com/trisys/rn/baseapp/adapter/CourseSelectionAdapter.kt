package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import kotlinx.android.synthetic.main.list_courses.view.*

class CourseSelectionAdapter(
    val context: Context,
    private val scheduledTestItems: ArrayList<String>?,
) : RecyclerView.Adapter<CourseSelectionAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.list_courses, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scheduledTest = scheduledTestItems?.get(position)
        holder.itemView.txt_course.text = scheduledTest
        holder.itemView.txt_course.setOnClickListener {
            holder.itemView.txt_course.setBackgroundResource(R.drawable.ic_rectangle_blue)
        }
    }

    override fun getItemCount(): Int {
        scheduledTestItems.let {
            return scheduledTestItems!!.size
        }
    }
}