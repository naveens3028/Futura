package com.trisys.rn.baseapp.practiceTest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jstarczewski.pc.mathview.src.TextAlign
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.AnswerClickListener
import com.trisys.rn.baseapp.model.AnswerChooseItem
import kotlinx.android.synthetic.main.row_answer_choose_list.view.*


class AnswerChooseAdapter(
    val context: Context,
    private val answerChooseItem: ArrayList<AnswerChooseItem>,
    private val answer: String?,
    private val answerClickListener: AnswerClickListener,
    private val questionPosition: Int,
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

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answerItem = answerChooseItem[position]
        if (answerItem.isSelected) previousPosition = position

        val ans = answerItem.answer!!.replace("\n", "").replace("<p class=\\\"p4\\\">", "")
        holder.itemView.mvTest.apply {
            textZoom = 60
            textColor = Color.GREEN.toString()
            textAlign = TextAlign.LEFT
            backgroundColor = "#EEF4FA"
            text = ans
        }

        if (!isReview) {
            holder.itemView.setOnClickListener {
                if (!answerItem.isSelected) {
                    if (previousPosition > -1) {
                        answerChooseItem[previousPosition].isSelected = false
                        notifyItemChanged(previousPosition)
                    }
                    answerItem.isSelected = true
                    notifyItemChanged(position)
                    val answer: Char = (position + 97).toChar()
                    answerClickListener.onAnswerClicked(true, answer, questionPosition)
                } else {
                    answerItem.isSelected = false
                    notifyItemChanged(position)
                    answerClickListener.onAnswerClicked(false, '-', questionPosition)
                }
            }
            holder.itemView.ansView.setOnClickListener {
                if (!answerItem.isSelected) {
                    if (previousPosition > -1) {
                        answerChooseItem[previousPosition].isSelected = false
                        notifyItemChanged(previousPosition)
                    }
                    answerItem.isSelected = true
                    notifyItemChanged(position)
                    val answer: Char = (position + 97).toChar()
                    answerClickListener.onAnswerClicked(true, answer, questionPosition)
                } else {
                    answerItem.isSelected = false
                    notifyItemChanged(position)
                    answerClickListener.onAnswerClicked(false, '-', questionPosition)
                }
            }
        }
        if (!answer.isNullOrEmpty()) {
            val ansPosition = answer[0].code.minus(97)
            if (ansPosition == position && previousPosition < 0) {
                previousPosition = position
                answerItem.isSelected = true
            }
        }


        if (answerItem.isSelected) {
            holder.itemView.answer.setImageResource(R.drawable.ic_check_circle)
            holder.itemView.parentView.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.tea_green
                )
            )
            holder.itemView.mvTest.apply {
                backgroundColor = "#D5FBD3"
            }
        } else if (isReview && position == 1) {
            holder.itemView.answer.setImageResource(R.drawable.ic_close_outline)
            holder.itemView.parentView.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.pale_pink
                )
            )
            holder.itemView.mvTest.apply {
                backgroundColor = "#FBD3D3"
            }
        } else {
            holder.itemView.answer.setImageResource(R.drawable.ic_circle_outline)
            holder.itemView.parentView.background.setTint(
                ContextCompat.getColor(
                    context,
                    R.color.alice_blue
                )
            )
            holder.itemView.mvTest.apply {
                backgroundColor = "#EEF4FA"
            }
        }
    }

    override fun getItemCount(): Int {
        return answerChooseItem.size
    }
}