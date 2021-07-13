package com.trisys.rn.baseapp.practiceTest

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.SystemClock
import android.view.*
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.AnswerClickListener
import com.trisys.rn.baseapp.database.DatabaseHelper
import com.trisys.rn.baseapp.model.*
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.network.URLHelper.getStudentTestPaper
import com.trisys.rn.baseapp.network.URLHelper.testPaperForStudent
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionAdapter
import com.trisys.rn.baseapp.practiceTest.adapter.QuestionNumberAdapter
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_today_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.*
import kotlinx.android.synthetic.main.fragment_learn.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class TodayTestActivity : AppCompatActivity(), OnNetworkResponse, AnswerClickListener {

    private val questionNumberItem = ArrayList<QuestionNumberItem>()
    private lateinit var questionNumberAdapter: QuestionNumberAdapter
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    lateinit var testPaperId: String
    lateinit var studentId: String
    lateinit var testName: String
    private lateinit var db: DatabaseHelper
    lateinit var questionList: List<Quesion>
    lateinit var date: String
    var noMarked: Int = 0
    var currentPosition = 0
    private lateinit var testPaperResponse: TestPaperResponse
    private lateinit var testStudentResponse: TestPaperForStudentResponse

    private lateinit var questionAdapter: QuestionAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_today_test)

        testPaperId = intent.extras?.getString("testPaperId") ?: ""
        studentId = intent.extras?.getString("studentId") ?: ""
        testName = intent.extras?.getString("testName") ?: ""
        date = intent.extras?.getString("date") ?: ""

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
        getTest()

        questionGroup.setOnClickListener {
            showDialog()
        }

        submitTest.setOnClickListener {
            val intent = Intent(this, TestReviewActivity::class.java)
            startActivity(intent)
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
                if (currentPosition < position)
                    saveNext(position - 1)
                else if (currentPosition > position)
                    saveNext(position + 1)
                currentPosition = position
//                val start = System.currentTimeMillis()
//                val runTime = System.currentTimeMillis() - start
            }

        })
        markForReview.setOnClickListener {
            questionNumberItem[viewPager.currentItem].questionType = QuestionType.MARK_FOR_REVIEW
            noMarked += 1
            markedValue.text = noMarked.toString()
            viewPager.currentItem = viewPager.currentItem++
        }

        next.setOnClickListener {
            viewPager.currentItem++
        }
        previous.setOnClickListener {
            viewPager.currentItem--
        }
        submitTest.setOnClickListener {
            submitTestPaper()
        }

    }

    private fun submitTestPaper() {
        val jsonObject = JSONObject()
        try {
            jsonObject.put("testPaperId", testPaperId)
            jsonObject.put("attempt", testStudentResponse.data.testPaper.attempts)
            jsonObject.put("studentId", studentId)
            jsonObject.put("testDurationTime", testStudentResponse.data.testPaper.duration)
            val jsonArray = JSONArray()
            val jsonAnsObject = JSONObject()
            jsonAnsObject.put(
                "questionPaperId",
                questionList[viewPager.currentItem].id
            )
            jsonAnsObject.put(
                "answer",
                questionList[viewPager.currentItem].optionSelected
            )
            jsonAnsObject.put(
                "timeSpent",
                questionList[viewPager.currentItem].timeSpent
            )
            jsonArray.put(jsonAnsObject)
            jsonObject.put("questionAnswerList", jsonArray)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        statefulLayout.showProgress()
        statefulLayout.setProgressText("Loading..")
        networkHelper.postCall(
            URLHelper.submitTestPaper,
            jsonObject,
            "submitTestPaper",
            ApiUtils.getHeader(this),
            this
        )
    }

    private fun saveNext(position: Int) {
        if (position >= 0 && !questionList[position].optionSelected.isNullOrEmpty()) {
            val noCompleted = questionList.filter { it.isAnswered }.size
            completedValue.text =
                "$noCompleted Out of ${testStudentResponse.data.testPaper.questionCount}"
            if (questionList[position].optionSelected == "-") {
                questionNumberItem[position].questionType = QuestionType.NOT_ATTEMPT
            } else if (questionList[position].optionSelected!!.isEmpty()) {
                questionNumberItem[position].questionType = QuestionType.ATTEMPT
            }
            questionNumberAdapter.setItems(questionNumberItem)
            questionNumberAdapter.notifyDataSetChanged()

            val jsonObject = JSONObject()
            try {
                jsonObject.put("testPaperId", testPaperId)
                jsonObject.put("attempt", testStudentResponse.data.testPaper.attempts)
                jsonObject.put("studentId", studentId)
                jsonObject.put("testDurationTime", testStudentResponse.data.testPaper.duration)
                jsonObject.put(
                    "questionPaperId",
                    questionList[viewPager.currentItem].id
                )
                jsonObject.put(
                    "answer",
                    questionList[viewPager.currentItem].optionSelected
                )
                jsonObject.put(
                    "timeSpent",
                    questionList[viewPager.currentItem].timeSpent
                )
            } catch (e: JSONException) {
                e.printStackTrace()
            }
            networkHelper.postCall(
                URLHelper.next,
                jsonObject,
                "next",
                ApiUtils.getHeader(this),
                this
            )
        } else {
            try {
                if (questionNumberItem[position].questionType != QuestionType.MARK_FOR_REVIEW) {
                    questionNumberItem[position].questionType = QuestionType.NOT_ATTEMPT
                }
                questionNumberAdapter.setItems(questionNumberItem)
                questionNumberAdapter.notifyDataSetChanged()
            } catch (e: IndexOutOfBoundsException) {

            }

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
        val manager = FlexboxLayoutManager(this, FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER
        dialog.questionNumber.layoutManager = manager
        val questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem)
        dialog.questionNumber.adapter = questionNumberAdapter
        dialog.show()
    }

    private fun getTest() {
        statefulLayout.showProgress()
        statefulLayout.setProgressText("Loading..")
        networkHelper.getCall(
            getStudentTestPaper + "?testPaperId=$testPaperId&studentId=$studentId",
            "getStudentTestPaper",
            ApiUtils.getHeader(this),
            this
        )
        networkHelper.getCall(
            testPaperForStudent + "?testPaperId=$testPaperId&userDetailId=$studentId",
            "testPaperForStudent",
            ApiUtils.getHeader(this),
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (tag == "getStudentTestPaper") {
            statefulLayout.showContent()
            durationValue.start()
            if (responseCode == networkHelper.responseSuccess) {
                testPaperResponse = Gson().fromJson(response, TestPaperResponse::class.java)
                questionList = questionList
                assignQuestionOnline()
            } else {
                assignQuestionOffline()
            }

        } else if (responseCode == networkHelper.responseSuccess && tag == "testPaperForStudent") {
            testStudentResponse =
                Gson().fromJson(response, TestPaperForStudentResponse::class.java)
            actionBar?.title = testStudentResponse.data.testPaper.name
            durationValue.base =
                SystemClock.elapsedRealtime() + (testStudentResponse.data.testPaper.duration * 10000)
            if (testStudentResponse.data.testPaper.timeLeft > 0) {
                durationValue.base =
                    SystemClock.elapsedRealtime() + (testStudentResponse.data.testPaper.timeLeft * 10000)
            }
            completedValue.text =
                "0 Out of ${testStudentResponse.data.testPaper.questionCount}"
            markedValue.text = noMarked.toString()
            if (testStudentResponse.data.testPaper.isPauseAllow) {
                pause.isEnabled = true
                pause.alpha = 0f
            } else {
                pause.isEnabled = false
                pause.alpha = 0.6f
            }
            pause.isEnabled = testStudentResponse.data.testPaper.isPauseAllow
            formQuestionItem(testStudentResponse.data.testPaper.questionCount)
        } else if (tag == "submitTestPaper") {
            statefulLayout.showContent()
            if (responseCode == networkHelper.responseSuccess) {
                finish()
            }

        }
    }

    private fun formQuestionItem(questionCount: Int) {
        for (i in 1..questionCount) {
            questionNumberItem.add(QuestionNumberItem(i, QuestionType.NOT_VISITED))
        }
        questionNumberAdapter = QuestionNumberAdapter(this, questionNumberItem)
        questionNumberRecycler.adapter = questionNumberAdapter
    }

    private fun assignQuestionOnline() {
        questionAdapter = QuestionAdapter(this, questionList, this, false)
        viewPager.adapter = questionAdapter
        viewPager.offscreenPageLimit = 15
    }

    private fun assignQuestionOffline() {
        questionList = db.getQuestionList(testPaperId)
        formQuestionItem(questionList.size)
        questionAdapter = QuestionAdapter(this, questionList, this, false)
        viewPager.adapter = questionAdapter
        viewPager.offscreenPageLimit = 15
    }

    override fun onAnswerClicked(isClicked: Boolean, option: Char, position: Int) {
        questionList[position].isAnswered = isClicked
        questionList[position].optionSelected = option.toString()
    }

}