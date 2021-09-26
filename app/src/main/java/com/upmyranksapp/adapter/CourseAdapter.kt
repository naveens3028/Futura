package com.upmyranksapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.upmyranksapp.R
import com.upmyranksapp.fragment.practiceTest.CourseListener
import com.upmyranksapp.model.onBoarding.batchItem
import com.upmyranksapp.utils.ColorConstant
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
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.activity_course, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        holder.subjectTxt.text = courseList[position].course!!.courseName

        when (position) {
            0 -> {
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
            }
        }
        holder.subjectTxt.setOnClickListener {
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