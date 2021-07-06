package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.Datum

class SubjectsAdapter(
    val context: Context,
    val subjects: ArrayList<Datum>,
    var subjectClickListener: SubjectClickListener
) :
    RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTxt = itemView.findViewById(R.id.subjecttxt) as TextView
        val cardview = itemView.findViewById(R.id.cardsubject) as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_subjects, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subjectTxt.text = (subjects.get(position).courseName)
        holder.cardview.setOnClickListener {
            subjectClickListener.onSubjectClicked(true)
        }
    }

    fun Int.dpToPixels(context: Context): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    )

    override fun getItemCount(): Int {
        return subjects.size
    }
}