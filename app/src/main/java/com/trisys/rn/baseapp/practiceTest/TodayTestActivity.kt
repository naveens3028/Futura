package com.trisys.rn.baseapp.practiceTest

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.model.AnswerChooseItem
import com.trisys.rn.baseapp.model.QuestionItem
import com.trisys.rn.baseapp.model.QuestionNumberItem
import com.trisys.rn.baseapp.model.QuestionType
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionAdapter
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionNumberAdapter
import kotlinx.android.synthetic.main.activity_today_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class TodayTestActivity : AppCompatActivity() {

    private val questionNumberItem = ArrayList<QuestionNumberItem>()
    private val questionItems = ArrayList<QuestionItem>()
    private val answerChooseItem = ArrayList<AnswerChooseItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_test)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.subtitle = "19th Mar,12:04PM"

        questionGroup.setOnClickListener {
            showDialog()
        }

        questionNumberItem.add(QuestionNumberItem(1, QuestionType.ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(2, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(3, QuestionType.MARK_FOR_REVIEW))
        questionNumberItem.add(QuestionNumberItem(4, QuestionType.NOT_VISITED))
        questionNumberItem.add(QuestionNumberItem(5, QuestionType.NOT_VISITED))
        questionNumberItem.add(QuestionNumberItem(6, QuestionType.NOT_VISITED))
        questionNumberItem.add(QuestionNumberItem(7, QuestionType.NOT_VISITED))
        questionNumberItem.add(QuestionNumberItem(8, QuestionType.NOT_VISITED))
        questionNumberItem.add(QuestionNumberItem(9, QuestionType.NOT_VISITED))
        questionNumberItem.add(QuestionNumberItem(10, QuestionType.NOT_VISITED))
        val questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem)
        questionNumberRecycler.adapter = questionNumberAdapter

        answerChooseItem.add(AnswerChooseItem("a). Has to reduced."))
        answerChooseItem.add(AnswerChooseItem("b). Has to be increased."))
        answerChooseItem.add(AnswerChooseItem("c). Needs no adjustment."))
        questionItems.add(
            QuestionItem(
                "A pendulum clock is set to give correct time at the sea level. This clock is moved to hill station at an altitude of above sea level. In order to keep correct time of hill station, the length the pendulum",
                answerChooseItem
            )
        )
        questionItems.add(
            QuestionItem(
                "A pendulum clock is set to give correct time at the sea level. This clock is moved to hill station at an altitude of above sea level. In order to keep correct time of hill station, the length the pendulum",
                answerChooseItem
            )
        )
        questionItems.add(
            QuestionItem(
                "A pendulum clock is set to give correct time at the sea level. This clock is moved to hill station at an altitude of above sea level. In order to keep correct time of hill station, the length the pendulum",
                answerChooseItem
            )
        )
        val questionAdapter = QuestionAdapter(this, questionItems, false)
        viewPager.adapter = questionAdapter
        submitTest.setOnClickListener {
            val intent = Intent(this, TestReviewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_learn, menu)
            val item1 =
                menu.findItem(R.id.action_menu_notification).actionView.findViewById(R.id.layoutNotification) as RelativeLayout
            item1.setOnClickListener {
                startActivity(Intent(this, NotificationsActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_jump_to_questions)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.window!!.setGravity(Gravity.CENTER)
        dialog.window!!.attributes.gravity = Gravity.CENTER
        dialog.window!!.setLayout(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog.close.setOnClickListener {
            dialog.cancel()
            dialog.hide()
        }
        val questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem)
        dialog.questionNumber.adapter = questionNumberAdapter
        dialog.show()
    }
}