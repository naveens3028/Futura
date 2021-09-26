package com.upmyranksapp.fragment.practiceTest

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.activity.TakeTestActivity
import com.upmyranksapp.adapter.ScheduledTestAdapter
import com.upmyranksapp.adapter.TestClickListener
import com.upmyranksapp.database.DatabaseHelper
import com.upmyranksapp.model.MOCKTEST
import com.upmyranksapp.model.ScheduledClass
import com.upmyranksapp.model.onBoarding.AttemptedTest
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.ApiUtils
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import com.upmyranksapp.utils.Utils
import com.upmyranksapp.utils.Utils.LAUNCH_SECOND_ACTIVITY
import kotlinx.android.synthetic.main.dialog_confirm_test.*
import kotlinx.android.synthetic.main.fragment_scheduled_test.*

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
        //requestTest()
    }

    override fun onTestClicked(isClicked: Boolean, mockTest: MOCKTEST) {
        showDialog(mockTest)
    }

    private fun showDialog(mockTest: MOCKTEST) {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.setContentView(R.layout.dialog_confirm_test)
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
        dialog.disagree.setOnClickListener {
            dialog.cancel()
            dialog.hide()
        }
        dialog.agree.setOnClickListener {
            val intent = Intent(requireContext(), TakeTestActivity::class.java)
            intent.putExtra("duration", mockTest.testPaperVo?.duration)
            intent.putExtra("timeLeft", mockTest.testPaperVo?.timeLeft)
            intent.putExtra("questionCount", mockTest.testPaperVo?.questionCount.toString())
            intent.putExtra("noAttempted", mockTest.testPaperVo?.attempts.toString())
            intent.putExtra("date", Utils.getDateValue(mockTest.publishDateTime))
            intent.putExtra("testPaperId", mockTest.testPaperId)
            intent.putExtra("testPaperName", mockTest.testPaperVo?.name)
            intent.putExtra("isPauseAllow", mockTest.testPaperVo?.isPauseAllow)
            startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
            dialog.cancel()
            dialog.hide()
            myPreferences.setBoolean(Define.TAKE_TEST_MODE_OFFLINE, true)
            goToTestScreen(mockTest)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun goToTestScreen(mockTest: MOCKTEST){
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        intent.putExtra("duration", mockTest.testPaperVo?.duration)
        intent.putExtra("timeLeft", mockTest.testPaperVo?.timeLeft)
        intent.putExtra("questionCount", mockTest.testPaperVo?.questionCount.toString())
        intent.putExtra("noAttempted", mockTest.testPaperVo?.attempts.toString())
        intent.putExtra("date", Utils.getDateValue(mockTest.publishDateTime))
        intent.putExtra("testPaperId", mockTest.testPaperId)
        intent.putExtra("testPaperName", mockTest.testPaperVo?.name)
        intent.putExtra("isPauseAllow", mockTest.testPaperVo?.isPauseAllow)
        startActivityForResult(intent, LAUNCH_SECOND_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                //requestTest()
            }
        }
    }

    override fun onResultClicked(id: String) {

    }

    override fun onResultClicked(attempt: Int, studentId: String, testPaperId: String) {

    }

    override fun onReviewClicked(attempt: AttemptedTest) {
        TODO("Not yet implemented")
    }


/*
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
*/

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        try {
            if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {
                val scheduledTestResponse =
                    Gson().fromJson(response, ScheduledClass::class.java)
                if (scheduledTestResponse.MOCK_TEST.isNullOrEmpty()) {
                    recycler.visibility = View.GONE
                    noData.visibility = View.VISIBLE
                } else {
                    recycler.visibility = View.VISIBLE
                    noData.visibility = View.GONE
                    val completedList = db.getCompletedTest()
                    completedList.forEachIndexed { _, completedListElement ->
                        scheduledTestResponse.MOCK_TEST =
                            scheduledTestResponse.MOCK_TEST.filterNot { it.testPaperId == completedListElement.testPaperId }
                    }
                    val scheduledTestAdapter = ScheduledTestAdapter(
                        requireView().context,
                        scheduledTestResponse.MOCK_TEST,
                        this
                    )
                    recycler.adapter = scheduledTestAdapter
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkHelper.cancel("scheduledTest")
    }
}
