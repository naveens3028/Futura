package com.upmyranksapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.R
import com.upmyranksapp.fragment.practiceTest.CourseListener
import com.upmyranksapp.model.onBoarding.batchItem
import java.util.*

class CourseAdapter(
    val context: Context,
    private val courseListener: CourseListener,
    val courseList: ArrayList<batchItem>
) :
    RecyclerView.Adapter<CourseAdapter.ViewHolder>() {
    private var index = 0

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTxt = itemView.findViewById(R.id.coursetxt) as TextView
        val rltLyt = itemView.findViewById(R.id.rltLyt) as RelativeLayout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_course, parent, false)
        return ViewHolder(v)
    }

    @SuppressLint("ResourceType")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.subjectTxt.text = courseList[position].course!!.courseName
        holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.purple_500))

        when (position) {
            index ->{
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.rltLyt.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500))
            }
            else ->{
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.purple_500))
                holder.rltLyt.setBackgroundColor(ContextCompat.getColor(context, R.color.White))

            }
           /* 0 -> {
                holder.subjectTxt.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        ColorConstant.NCERT
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                if (index == 0) {
                    holder.subjectTxt.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            ColorConstant.SELECTED
                        )
                    )
                    holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }

            1 -> {
                holder.subjectTxt.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        ColorConstant.NEET
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))

                if (index == 1) {
                    holder.subjectTxt.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }
            2 -> {
                holder.subjectTxt.setBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        ColorConstant.JEE_MAINS
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))

                if (index == 2) {
                    holder.subjectTxt.setBackgroundColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                    holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            }*/

        }

        holder.subjectTxt.setOnClickListener {
            holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.black))

            if (courseList[position].additionalCourseId.isNullOrEmpty()) {
                courseList[position].let { it1 ->
                    it1.courseId?.let { it2 ->
                        it1.id?.let { it3 ->
                            courseListener.onCourseClicked(
                                it2, it3, position
                            )
                        }
                    }
                }
                index = position
                notifyDataSetChanged()
            } else {
                courseList[position].let { it1 ->
                    it1.additionalCourseId?.let { it2 ->
                        it1.id?.let { it3 ->
                            courseListener.onCourseClicked(
                                it2, it3, position
                            )
                        }
                    }
                }
                index = position
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

}