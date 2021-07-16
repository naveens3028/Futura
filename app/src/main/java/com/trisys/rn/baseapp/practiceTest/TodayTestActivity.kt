package com.trisys.rn.baseapp.practiceTest

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.AnswerClickListener
import com.trisys.rn.baseapp.database.DatabaseHelper
import com.trisys.rn.baseapp.model.Quesion
import com.trisys.rn.baseapp.model.QuestionNumberItem
import com.trisys.rn.baseapp.model.QuestionType
import com.trisys.rn.baseapp.network.*
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionAdapter
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionNumberAdapter
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_today_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.json.JSONArray
import org.json.JSONObject


class TodayTestActivity : AppCompatActivity(), OnNetworkResponse, AnswerClickListener,
    QuestionClickListener {

    private val questionNumberItem = ArrayList<QuestionNumberItem>()
    private lateinit var questionNumberAdapter: QuestionNumberAdapter
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    lateinit var testPaperId: String
    lateinit var studentId: String
    lateinit var testName: String
    private lateinit var db: DatabaseHelper
    lateinit var cd: ConnectionDetector
    lateinit var questionList: List<Quesion>
    lateinit var date: String
    var noMarked: Int = 0
    var currentPosition = 0
    private var isPauseAllow = false
    private var testDuration = 0
    private var attemptedValue = ""
    private lateinit var dialog: Dialog

    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_test)

        testPaperId = intent.extras?.getString("testPaperId") ?: ""
        studentId = intent.extras?.getString("studentId") ?: ""
        testName = intent.extras?.getString("testName") ?: ""
        date = intent.extras?.getString("date") ?: ""
        isPauseAllow = intent.extras?.getBoolean("isPauseAllow")!!
        val timeLeft = intent.extras?.getInt("timeLeft") ?: 0
        testDuration = intent.extras?.getInt("duration") ?: 0
        attemptedValue = intent.extras?.getString("attemptedValue") ?: ""
        durationValue.base =
            SystemClock.elapsedRealtime() + testDuration.times(10000)
        if (timeLeft > 0) {
            durationValue.base =
                SystemClock.elapsedRealtime() + (timeLeft * 10000)
        }

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = testName
        actionBar?.subtitle = date

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
        db = DatabaseHelper(this)
        cd = ConnectionDetector(this)

        markReview()

        questionList = db.getQuestionList(testPaperId)
        val noCompleted = questionList.filter {
            !it.answer.isNullOrEmpty() && it.answer != "-"
        }.size
        val completedValueText = "$noCompleted Out of ${questionList.size}"
        completedValue.text = completedValueText
        formQuestionItem(questionList.size)

        dialog = Dialog(this)
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
        val manager = FlexboxLayoutManager(this, FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER
        dialog.questionNumber.layoutManager = manager
        questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem, this)
        dialog.questionNumber.adapter = questionNumberAdapter

        if (isPauseAllow) {
            pause.isEnabled = true
            pause.alpha = 0f
        } else {
            pause.isEnabled = false
            pause.alpha = 0.6f
        }
        pause.isEnabled = isPauseAllow

        questionGroup.setOnClickListener {
            showDialog()
        }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                questionNumberRecycler.layoutManager?.scrollToPosition(position)
            }

            override fun onPageSelected(position: Int) {
                if (currentPosition == position - 1)
                    saveNext(position - 1)
                else if (currentPosition - 1 == position)
                    saveNext(position + 1)
                currentPosition = position
                /*val start = System.currentTimeMillis()
                val runTime = System.currentTimeMillis() - start*/
            }
        })
        markForReview.setOnClickListener {
            Utils.testLog("Oif")
            if (questionNumberItem[viewPager.currentItem].questionType == QuestionType.NOT_ATTEMPT || questionNumberItem[viewPager.currentItem].questionType == QuestionType.NOT_VISITED) {
                Utils.testLog("if")
                ++noMarked
                markReview()
                questionNumberItem[viewPager.currentItem].questionType =
                    QuestionType.MARK_FOR_REVIEW
                questionNumberAdapter.setItems(questionNumberItem)
                questionNumberAdapter.notifyDataSetChanged()
            }
            viewPager.currentItem++
        }

        assignQuestion()

        next.setOnClickListener {
            viewPager.currentItem++
        }
        previous.setOnClickListener {
            viewPager.currentItem--
        }
        submitTest.setOnClickListener {
            submitTestPaper()
        }
        durationValue.start()
    }

    private fun markReview() {
        markedValue.text = noMarked.toString()
    }

    private fun submitTestPaper() {
        statefulLayout.showProgress()
        statefulLayout.setProgressText("Loading..")
        if (cd.isConnectingToInternet()) {
            val jsonObject = JSONObject()
            jsonObject.put("testPaperId", testPaperId)
            jsonObject.put("attempt", attemptedValue)
            jsonObject.put("studentId", studentId)
            jsonObject.put("testDurationTime", testDuration)
            val jsonArray = JSONArray()
            for (question in questionList) {
                val jsonAnsObject = JSONObject()
                jsonAnsObject.put(
                    "questionPaperId",
                    question.id
                )
                jsonAnsObject.put(
                    "answer",
                    question.answer
                )
                jsonAnsObject.put(
                    "timeSpent",
                    question.timeSpent
                )
                jsonArray.put(jsonAnsObject)
            }
            jsonObject.put("questionAnswerList", jsonArray)
            networkHelper.postCall(
                URLHelper.submitTestPaper,
                jsonObject,
                "submitTestPaper",
                ApiUtils.getHeader(this),
                this
            )

        } else {
            for (question in questionList) {
                db.updateAnswer(question.id, question.answer)
            }
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun saveNext(position: Int) {
        if (questionList[position].answer.isNullOrEmpty()) {
            if (questionNumberItem[position].questionType != QuestionType.MARK_FOR_REVIEW) {
                questionNumberItem[position].questionType = QuestionType.NOT_ATTEMPT
            }
            questionNumberAdapter.setItems(questionNumberItem)
            questionNumberAdapter.notifyDataSetChanged()
        } else {
            val noCompleted =
                questionList.filter { !it.answer.isNullOrEmpty() && it.answer != "-" }.size
            val completedValueText = "$noCompleted Out of ${questionList.size}"
            completedValue.text = completedValueText
            when {
                questionNumberItem[position].questionType == QuestionType.MARK_FOR_REVIEW -> {

                }
                questionList[position].answer == "-" -> {
                    questionNumberItem[position].questionType = QuestionType.NOT_ATTEMPT
                }
                else -> {
                    questionNumberItem[position].questionType = QuestionType.ATTEMPT
                }
            }
            questionNumberAdapter.setItems(questionNumberItem)
            questionNumberAdapter.notifyDataSetChanged()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> Snackbar.make(
                window.decorView.rootView,
                "Test is going you are not able to go back",
                Snackbar.LENGTH_LONG
            ).show()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showDialog() {
        dialog.show()
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        statefulLayout.showContent()
        if (responseCode == networkHelper.responseSuccess && tag == "submitTestPaper") {
            val returnIntent = Intent()
            setResult(RESULT_OK, returnIntent)
            finish()
        }
    }

    private fun formQuestionItem(questionCount: Int) {
        for (i in 1..questionCount) {
            if (questionList[i - 1].answer.isNullOrEmpty())
                questionNumberItem.add(QuestionNumberItem(i, QuestionType.NOT_VISITED))
            else
                questionNumberItem.add(QuestionNumberItem(i, QuestionType.ATTEMPT))
        }
        questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem, this)
        questionNumberRecycler.adapter = questionNumberAdapter
    }

    private fun assignQuestion() {
        questionAdapter = QuestionAdapter(this, questionList, this, false)
        viewPager.adapter = questionAdapter
        viewPager.offscreenPageLimit = 15
    }

    override fun onAnswerClicked(isClicked: Boolean, option: Char, position: Int) {
        questionList[position].isAnswered = isClicked
        if (!isClicked) {
            questionNumberItem[position].questionType = QuestionType.NOT_ATTEMPT
            questionNumberAdapter.setItems(questionNumberItem)
            questionNumberAdapter.notifyDataSetChanged()
        }
        questionList[position].answer = option.toString()
        if (questionNumberItem[position].questionType == QuestionType.MARK_FOR_REVIEW && option.toString()
                .isNotEmpty()
        ) {
            noMarked--
            markReview()
        }
    }

    override fun onBackPressed() {
        Snackbar.make(
            window.decorView.rootView,
            "Test is going you are not able to go back",
            Snackbar.LENGTH_LONG
        ).show()
    }

    override fun onQuestionClicked(position: Int) {
        if (dialog.isShowing) {
            dialog.cancel()
            dialog.hide()
        }
        saveNext(viewPager.currentItem)
        viewPager.currentItem = position
    }

}