package com.trisys.rn.baseapp.practiceTest.adapter

import android.content.Context
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.AnswerClickListener
import com.trisys.rn.baseapp.model.AnswerChooseItem
import com.trisys.rn.baseapp.model.Quesion
import kotlinx.android.synthetic.main.row_question_list.view.*


class QuestionAdapter(
    private val mContext: Context,
    private val questionItems: List<Quesion>,
    private val answerClickListener: AnswerClickListener,
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
        itemView.question.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(item.questionContent, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(item.questionContent)
        }
        val answerChooseItem = ArrayList<AnswerChooseItem>()

        answerChooseItem.add(AnswerChooseItem("a). ${item.optionA}"))
        answerChooseItem.add(AnswerChooseItem("b). ${item.optionB}"))
        answerChooseItem.add(AnswerChooseItem("c). ${item.optionC}"))
        answerChooseItem.add(AnswerChooseItem("d). ${item.optionD}"))
        itemView.answerChoose.adapter = AnswerChooseAdapter(mContext, answerChooseItem,answerClickListener,position, isReview)
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {}

}