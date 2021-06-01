package com.trisys.rn.baseapp.practiceTest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.AnswerChooseItem
import kotlinx.android.synthetic.main.row_answer_choose_list.view.*


class AnswerChooseAdapter(
    val context: Context,
    private val answerChooseItem: ArrayList<AnswerChooseItem>,
    private val isReview: Boolean
) : RecyclerView.Adapter<AnswerChooseAdapter.ViewHolder>() {

    private var previousPosition = -1

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.row_answer_choose_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answerItem = answerChooseItem[position]

        if (answerItem.isSelected)
            previousPosition = holder.adapterPosition
        holder.itemView.answer.text = answerItem.answer

        if (!isReview) {
            holder.itemView.setOnClickListener {
                if (!answerItem.isSelected) {
                    if (previousPosition > -1) {
                        answerChooseItem[previousPosition].isSelected = false
                        notifyItemChanged(previousPosition)
                    }
                    answerItem.isSelected = true
                    notifyItemChanged(holder.adapterPosition)
                }
            }
        }

        if (answerItem.isSelected) {
            holder.itemView.answer.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_check_circle,
                0
            )
            holder.itemView.answer.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.tea_green
                )
            )
        } else if (isReview && position == 1) {
            holder.itemView.answer.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_close_outline,
                0
            )
            holder.itemView.answer.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.pale_pink
                )
            )
        } else {
            holder.itemView.answer.setCompoundDrawablesWithIntrinsicBounds(
                0,
                0,
                R.drawable.ic_circle_outline,
                0
            )
            holder.itemView.answer.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.alice_blue
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return answerChooseItem.size
    }
}