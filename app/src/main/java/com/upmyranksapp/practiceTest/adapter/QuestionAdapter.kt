package com.upmyranksapp.practiceTest.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.jstarczewski.pc.mathview.src.TextAlign
import com.upmyranksapp.R
import com.upmyranksapp.adapter.AnswerClickListener
import com.upmyranksapp.model.AnswerChooseItem
import com.upmyranksapp.model.Quesion
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
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = container.context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = inflater.inflate(R.layout.row_question_list, container, false) as View

        mContext.cacheDir.deleteRecursively()

        val item = questionItems[position]

        itemView.questionNumber.text = "Question: " + (position + 1)

        val question = item.questionContent?.replace("\n", "")?.replace("<p class=\\\"p4\\\">", "")
        itemView.question.apply {
            textZoom = 60
            textColor = Color.GREEN.toString()
            textAlign = TextAlign.LEFT
            text = question
        }

        val answerChooseItem = ArrayList<AnswerChooseItem>()

        answerChooseItem.add(AnswerChooseItem(item.optionA?.replace("\n", "")))
        answerChooseItem.add(AnswerChooseItem(item.optionB?.replace("\n", "")))
        answerChooseItem.add(AnswerChooseItem(item.optionC?.replace("\n", "")))
        answerChooseItem.add(AnswerChooseItem(item.optionD?.replace("\n", "")))
        itemView.answerChoose.adapter =
            AnswerChooseAdapter(
                mContext,
                answerChooseItem,
                item.answer,
                "",
                answerClickListener,
                position,
                isReview
            )
        container.addView(itemView, 0)
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

}