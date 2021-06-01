package com.trisys.rn.baseapp.practiceTest.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.QuestionItem
import kotlinx.android.synthetic.main.row_question_list.view.*


class QuestionAdapter(
    private val mContext: Context,
    private val questionItems: ArrayList<QuestionItem>,
    private val isReview: Boolean
) :
    PagerAdapter() {

    override fun getCount(): Int {
        return questionItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView =
            LayoutInflater.from(mContext).inflate(R.layout.row_question_list, container, false)

        val item = questionItems[position]

        itemView.questionNumber.text = "Question: " + (position + 1)
        itemView.question.text = item.question
        itemView.answerChoose.adapter = AnswerChooseAdapter(mContext, item.chooseList, isReview)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

}