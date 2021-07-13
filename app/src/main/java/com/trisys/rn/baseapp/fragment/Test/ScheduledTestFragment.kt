package com.trisys.rn.baseapp.fragment.Test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.TakeTestActivity
import com.trisys.rn.baseapp.adapter.ScheduledTestAdapter
import com.trisys.rn.baseapp.adapter.TestClickListener
import com.trisys.rn.baseapp.database.DatabaseHelper
import com.trisys.rn.baseapp.model.MergedTest
import com.trisys.rn.baseapp.model.ScheduledClass
import com.trisys.rn.baseapp.model.TestPaperResponse
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import com.trisys.rn.baseapp.utils.Utils.getDateValue
import com.trisys.rn.baseapp.utils.Utils.getDuration
import kotlinx.android.synthetic.main.fragment_scheduled_test.*
import kotlinx.android.synthetic.main.fragment_upcoming_live.recycler


class ScheduledTestFragment : Fragment(), TestClickListener, OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    private lateinit var db: DatabaseHelper
    lateinit var testPaperId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
        db = DatabaseHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scheduled_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        requestTest()
    }

    override fun onTestClicked(isClicked: Boolean, mergedTest: MergedTest) {
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        intent.putExtra("duration", getDuration(mergedTest.duration))
        intent.putExtra("questionCount", mergedTest.questionCount.toString())
        intent.putExtra("noAttempted", mergedTest.attempts.toString())
        intent.putExtra("date", getDateValue(mergedTest.publishDate))
        intent.putExtra("testPaperId", mergedTest.testPaperId)
        intent.putExtra("testPaperName", mergedTest.name)
        startActivity(intent)
    }

    override fun onResultClicked(isClicked: Boolean) {

    }

    override fun onResultClicked(attempt: Int, studentId: String, testPaperId: String) {

    }


    private fun requestTest() {

        networkHelper.getCall(
            URLHelper.scheduleTestsForStudent + "?batchId=${
                loginData.userDetail?.batchIds?.get(0)
            }&studentId=${loginData.userDetail?.usersId}",
            "scheduledTest",
            ApiUtils.getHeader(requireContext()),
            this
        )

    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (view != null) {
            if (responseCode == networkHelper.responseSuccess && tag == "getStudentTestPaper") {
                val testPaperResponse = Gson().fromJson(response, TestPaperResponse::class.java)
                for (question in testPaperResponse.quesionList) {
                    question.testPaperId = testPaperId
                    db.addQuestions(question)
                }
            }
            else if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {
                val scheduledTestResponse =
                    Gson().fromJson(response, ScheduledClass::class.java)
                if (scheduledTestResponse.MOCK_TEST.isNullOrEmpty()) {
                    if (db.getAllTest().isEmpty()) {
                        recycler.visibility = View.GONE
                        noData.visibility = View.VISIBLE
                    } else {
                        val scheduledTestAdapter = ScheduledTestAdapter(
                            requireView().context,
                            db.getAllTest(),
                            this
                        )
                        recycler.adapter = scheduledTestAdapter
                    }
                } else {
                    db.deleteTestList()
                    for (mockTest in scheduledTestResponse.MOCK_TEST) {
                        getTest(mockTest.testPaperId)
                        db.saveTestList(mockTest)
                        db.saveTestPaper(mockTest.testPaperVo!!)
                    }
                    val scheduledTestAdapter = ScheduledTestAdapter(
                        requireView().context,
                        db.getAllTest(),
                        this
                    )
                    recycler.adapter = scheduledTestAdapter
                }
            } else {
                if (db.getAllTest().isEmpty())
                    Toast.makeText(requireContext(), "Data unable to load", Toast.LENGTH_LONG)
                        .show()
                else {
                    val scheduledTestAdapter = ScheduledTestAdapter(
                        requireView().context,
                        db.getAllTest(),
                        this
                    )
                    recycler.adapter = scheduledTestAdapter
                }
            }
        }
    }

    private fun getTest(testPaperId: String) {
        this.testPaperId = testPaperId
        networkHelper.getCall(
            URLHelper.getStudentTestPaper + "?testPaperId=$testPaperId&studentId=${loginData.userDetail?.userDetailId}",
            "getStudentTestPaper",
            ApiUtils.getHeader(requireContext()),
            this
        )
    }

}
