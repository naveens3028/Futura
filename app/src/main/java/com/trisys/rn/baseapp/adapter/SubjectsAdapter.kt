package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.Datum

class SubjectsAdapter(
    val context: Context,
    val subjects: ArrayList<Datum>,
    val batchIds: String,
    var subjectClickListener: SubjectClickListener
) :
    RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTxt = itemView.findViewById(R.id.subjectTxt) as TextView
        val cardView = itemView.findViewById(R.id.cardsubject) as CardView
        val subjectImg = itemView.findViewById(R.id.subjectimg) as ImageView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_subjects, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val items = subjects[position]
        holder.subjectTxt.text = items.courseName
        holder.cardView.setOnClickListener {
            subjectClickListener.onSubjectClicked(items.id.toString(),batchIds,items.courseName.toString())
        }

        when(items.courseName){
            "CHEMISTRY"->{
                holder.subjectImg.setImageResource(R.drawable.ic_chem)
            }
            "PHYSICS"->{
                holder.subjectImg.setImageResource(R.drawable.ic_physics)
            }
            "BIOLOGY"->{
                holder.subjectImg.setImageResource(R.drawable.ic_bio)
            }
            "TAMIL"->{
                holder.subjectImg.setImageResource(R.drawable.ic_tamil)
            }
            "URDU"->{
                holder.subjectImg.setImageResource(R.drawable.ic_urdu)
            }
            "SOCIAL SCIENCE"->{
                holder.subjectImg.setImageResource(R.drawable.ic_social)
            }
            "MATHEMATICS"->{
                holder.subjectImg.setImageResource(R.drawable.ic_math)
            }
            "ACCOUNTANCY"->{
                holder.subjectImg.setImageResource(R.drawable.ic_accoutancy)
            }
            "ENGLISH"->{
                holder.subjectImg.setImageResource(R.drawable.ic_english)
            }
            "HINDI"->{
                holder.subjectImg.setImageResource(R.drawable.ic_hindi)
            }
            "SCIENCE"->{
                holder.subjectImg.setImageResource(R.drawable.ic_science)
            }
            "KANNADA"->{
                holder.subjectImg.setImageResource(R.drawable.ic_kannada)
            }
            "HISTORY"->{
                holder.subjectImg.setImageResource(R.drawable.ic_history)
            }
            "ECONOMICS"->{
                holder.subjectImg.setImageResource(R.drawable.ic_economics)
            }
            "COMPUTER SCIENCE"->{
                holder.subjectImg.setImageResource(R.drawable.ic_comp_sci)
            }
        }

    }

    override fun getItemCount(): Int {
        return subjects.size
    }
}