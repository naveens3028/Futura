package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import java.util.*

class CourseAdapter(val context: Context, val courseList: ArrayList<String>) :
    RecyclerView.Adapter<CourseAdapter.ViewHolder>() {

    var Index = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTxt = itemView.findViewById(R.id.coursetxt) as TextView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_course, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.subjectTxt.text = courseList.get(position)

        when (holder.subjectTxt.text) {
            "NEET" -> {

                holder.subjectTxt.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bluishgreen
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                if (Index == 1) {
                    holder.subjectTxt.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }

            "NCERT" -> {
                holder.subjectTxt.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.blue
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))

                if (Index == 0) {
                    holder.subjectTxt.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
            "JEE MAINS" -> {
                holder.subjectTxt.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))

                if (Index == 2) {
                    holder.subjectTxt.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
        }
        holder.subjectTxt.setOnClickListener {
            Index = position
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

}