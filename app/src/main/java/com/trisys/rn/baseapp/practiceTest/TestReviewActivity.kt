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
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.AnswerClickListener
import com.trisys.rn.baseapp.model.*
import com.trisys.rn.baseapp.model.onBoarding.AttemptedTest
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionNumberAdapter
import com.trisys.rn.baseapp.practiceTest.adapter.ReviewAdapter
import kotlinx.android.synthetic.main.activity_test_review.*
import kotlinx.android.synthetic.main.activity_test_review.questionGroup
import kotlinx.android.synthetic.main.activity_test_review.questionNumberRecycler
import kotlinx.android.synthetic.main.activity_test_review.viewPager
import kotlinx.android.synthetic.main.activity_today_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.json.JSONObject

class TestReviewActivity : AppCompatActivity(), OnNetworkResponse, AnswerClickListener,QuestionClickListener {

    private val questionNumberItem = ArrayList<QuestionNumberItem>()
    private val questionItems = ArrayList<QuestionItem>()
    private val answerChooseItem = ArrayList<AnswerChooseItem>()
    private lateinit var dialog: Dialog
    lateinit var networkHelper: NetworkHelper
    lateinit var questionList: List<SectionQuestion?>
    private lateinit var questionNumberAdapter: QuestionNumberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_review)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.subtitle = "19th Mar,12:04PM"

        networkHelper = NetworkHelper(this)

        val attemptedTest: AttemptedTest? = intent.getParcelableExtra("AttemptedTest")
        requestAttemptedTest(attemptedTest)

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
       /* questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem, this)
        dialog.questionNumber.adapter = questionNumberAdapter*/

        questionGroup.setOnClickListener {
            dialog.show()
        }

        /*questionNumberItem.add(QuestionNumberItem(1, QuestionType.ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(2, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(3, QuestionType.MARK_FOR_REVIEW))
        questionNumberItem.add(QuestionNumberItem(4, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(5, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(6, QuestionType.ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(7, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(8, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(9, QuestionType.NOT_ATTEMPT))
        questionNumberItem.add(QuestionNumberItem(10, QuestionType.ATTEMPT))
        val questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem, )
        questionNumberRecycler.adapter = questionNumberAdapter*/

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
        /*val questionAdapter = QuestionAdapter(this, questionItems,true)
        viewPager.adapter = questionAdapter*/

    }

    private fun requestAttemptedTest(attemptedTest: AttemptedTest?) {


        val jsonObject = JSONObject()
        jsonObject.put("attempt", attemptedTest?.totalAttempts)
        jsonObject.put("studentId", attemptedTest?.studentId)
        jsonObject.put("testPaperId", attemptedTest?.testPaperId)

        networkHelper.postCall(
            URLHelper.answeredTestPapers,
            jsonObject,
            "answeredTestPapers",
            ApiUtils.getHeader(this),
            this
        )

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

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "answeredTestPapers") {
            val testResponseResult = Gson().fromJson(response, TestResultsModel::class.java)
            questionList = testResponseResult?.sectionsData?.get(0)?.sectionQuestion!!
            timeTaken.text = "${testResponseResult?.totalConsumeTime}s"
            topperTime.text = "${testResponseResult?.totalTimeTakenByTopper}s"
            assignQuestion()
            formQuestionItem(questionList.size)
        }
    }

    private fun assignQuestion() {
        val questionAdapter = ReviewAdapter(this, questionList, this, true)
        viewPager.adapter = questionAdapter
        viewPager.offscreenPageLimit = 15
    }

    private fun formQuestionItem(questionCount: Int) {
        for (i in 1..questionCount) {
            if (questionList[i - 1]?.submittedAnswered.isNullOrEmpty())
                questionNumberItem.add(QuestionNumberItem(i, QuestionType.NOT_ATTEMPT))
            else if (questionList[i - 1]?.submittedAnswered.equals(
                    questionList[i - 1]?.correctAnswer,
                    ignoreCase = true
                )
            )
                questionNumberItem.add(QuestionNumberItem(i, QuestionType.ATTEMPT))
            else
                questionNumberItem.add(QuestionNumberItem(i, QuestionType.MARK_FOR_REVIEW))
        }
        questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem, this)
        questionNumberRecycler.adapter = questionNumberAdapter
    }

    override fun onAnswerClicked(isClicked: Boolean, option: Char, position: Int) {

    }

    override fun onQuestionClicked(position: Int) {
        if (dialog.isShowing) {
            dialog.cancel()
            dialog.hide()
        }
        viewPager.currentItem = position
    }

}