package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.helper.MyProgressBar
import com.trisys.rn.baseapp.model.TestResultsModel
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_test_results.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.json.JSONObject

class TestResultActivity : AppCompatActivity(), OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    private var testPaperId: String? = null
    private var attempt: Int? = null
    private var studentId: String? = null
    lateinit var myProgressBar: MyProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_results)

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
        myProgressBar = MyProgressBar(this)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        testPaperId = intent.getStringExtra("testPaperId")
        attempt = intent.getIntExtra("attempt", 0)
        studentId = intent.getStringExtra("studentId")


        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Test Result"

        getAttempt(attempt, studentId!!, testPaperId!!, "answeredTestPapersResult")

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
        if (tag == "answeredTestPapersResult") {
            val testResponseResult = Gson().fromJson(response, TestResultsModel::class.java)
            setValuestoUI(testResponseResult)
        }
    }

    private fun getAttempt(attempt: Int?, studentId: String, testPaperId: String, tag: String) {
        myProgressBar.show()
        val jsonObject = JSONObject()
        jsonObject.put("attempt", attempt.toString())
        jsonObject.put("studentId", studentId)
        jsonObject.put("testPaperId", testPaperId)

        networkHelper.postCall(
            URLHelper.answeredTestPapers,
            jsonObject,
            tag,
            ApiUtils.getHeader(this),
            this
        )
        val nightModeFlags: Int =
            applicationContext.resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        when (nightModeFlags) {
            Configuration.UI_MODE_NIGHT_YES -> {
                circletxtLeader.setTextColor(Color.parseColor("#FFFFFF"))
                yourScoreTxt.setTextColor(Color.parseColor("#FFFFFF"))
            }
            Configuration.UI_MODE_NIGHT_NO -> {
            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
            }
        }
    }


    private fun setValuestoUI(testResultsModel: TestResultsModel) {
        myProgressBar.dismiss()
        outofStudents.text = "Out of ${testResultsModel.totalRank} Students"
        yourScoreTxt.text = testResultsModel.totalObtainedMarks.toString()
        rankCircle.text = testResultsModel.currentRank.toString()
        outOfScoretxt.text = "Out of " + testResultsModel.totalQuestions.toString()
        correctAnsTxt.text = testResultsModel.totalCorrectMarks.toString()
        inCorrectAnsTxts.text = testResultsModel.totalWrongAttemptedQuestions.toString()
        unansweredTxtView.text = testResultsModel.totalUnAttemptedQuestons.toString()
        accuracTxtView.text = testResultsModel.accuracy
        totaltimeTxt.text = testResultsModel.totalConsumeTime.toString()
        avgtimePerQueTxt.text = testResultsModel.avgTimePerQuesByTopper.toString()
        toppertimeTxt.text = testResultsModel.totalTimeTakenByTopper.toString()
        totalQuestionAnsTxt.text = testResultsModel.totalAttemptedQuestions.toString()
    }

}