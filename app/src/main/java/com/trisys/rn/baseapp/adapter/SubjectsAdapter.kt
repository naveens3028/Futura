package com.trisys.rn.baseapp.adapter

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.model.Subjects
import com.trisys.rn.baseapp.R
import com.vpnews24.utils.ImageLoader

class SubjectsAdapter(
    val context: Context,
    val subjects: ArrayList<Subjects>,
    var subjectClickListener: SubjectClickListener
) :
    RecyclerView.Adapter<SubjectsAdapter.ViewHolder>() {

    private val imageloader = ImageLoader

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTxt = itemView.findViewById(R.id.subjecttxt) as TextView
        val subjectImg = itemView.findViewById(R.id.subjectimg) as ImageView
        val cardview = itemView.findViewById(R.id.cardsubject) as CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.activity_subjects, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subjectTxt.text = (subjects.get(position).subjects)
        imageloader.setImage(context, subjects.get(position).subjectLogo!!, holder.subjectImg)

        when (holder.subjectTxt.text) {
            "Physics" -> {
                holder.cardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.purple_300
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.cardview.setOnClickListener {
                    subjectClickListener.onSubjectClicked(true)
                }
            }

            "Chemistry" -> {
                holder.cardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.yellow
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.cardview.setOnClickListener {
                    subjectClickListener.onSubjectClicked(true)
                }
            }
            "Biology" -> {
                holder.cardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bio_pink
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.cardview.setOnClickListener {
                    subjectClickListener.onSubjectClicked(true)
                }
            }

            "Mathematics" -> {
                holder.cardview.setCardBackgroundColor(
                    ContextCompat.getColor(
                        context,
                        R.color.bluishgreen
                    )
                )
                holder.subjectTxt.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.cardview.setOnClickListener {
                    subjectClickListener.onSubjectClicked(true)
                }
            }
        }


    }

    fun Int.dpToPixels(context: Context): Float = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    )

    override fun getItemCount(): Int {
        return subjects.size
    }
}