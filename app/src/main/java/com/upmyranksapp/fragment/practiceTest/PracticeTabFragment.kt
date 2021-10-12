package com.upmyranksapp.fragment.practiceTest

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.activity.TakeTestActivity
import com.upmyranksapp.adapter.ScheduledTestAdapter
import com.upmyranksapp.adapter.TestClickListener
import com.upmyranksapp.adapter.test.AttemptedTestAdapter
import com.upmyranksapp.database.DatabaseHelper
import com.upmyranksapp.model.MOCKTEST
import com.upmyranksapp.model.PracticeSubjects
import com.upmyranksapp.model.ScheduledClass
import com.upmyranksapp.model.onBoarding.AttemptedResponse
import com.upmyranksapp.model.onBoarding.AttemptedTest
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.ApiUtils
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import com.upmyranksapp.utils.Utils
import kotlinx.android.synthetic.main.dialog_confirm_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.close
import kotlinx.android.synthetic.main.fragment_practice_tab.*
import kotlinx.android.synthetic.main.test_layout_child.view.*
import kotlinx.android.synthetic.main.test_layout_parent.view.*
import org.json.JSONArray
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class PracticeTabFragment : Fragment(), OnNetworkResponse, TestClickListener {

    private var subjectList = ArrayList<PracticeSubjects>()
    var attemptedTest: ArrayList<MOCKTEST>? = ArrayList()
    lateinit var db: DatabaseHelper
    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    var clickedTestPaperId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_practice_tab, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        db = DatabaseHelper(requireContext())
        networkHelper = NetworkHelper(requireContext())
        myPreferences = MyPreferences(requireContext())

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        requestTest()
    }

    override fun onStart() {
        super.onStart()

        expandablePracticeTest.parentLayout.txtParentTitle.text = "Practice Test"

        expandablePracticeTestAttempted.parentLayout.txtParentTitle.text = "Attempted Test"

        expandablePracticeTest.secondLayout.txtError.text = "No Test Found in Scheduled Test"

        expandablePracticeTestAttempted.secondLayout.txtError.text = "No Test Found in Attempted Test"

        expandablePracticeTest.parentLayout.setOnClickListener {
            if (expandablePracticeTest.isExpanded)
                expandablePracticeTest.collapse()
            else {
                expandablePracticeTest.expand()

            }
        }

        expandablePracticeTestAttempted.parentLayout.setOnClickListener {
            if(expandablePracticeTestAttempted.isExpanded)
                expandablePracticeTestAttempted.collapse()
            else {
                expandablePracticeTestAttempted.expand()
            }
        }

    }


    fun requestTest() {
        networkHelper.getCall(
            URLHelper.scheduleTestsForStudent + "?batchId=${
                loginData.userDetail?.batchList?.get(1)?.id
            }&studentId=${loginData.userDetail?.usersId}",
            "scheduledTest1",
            ApiUtils.getHeader(requireContext()),
            this
        )
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LearnFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PracticeTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.LAUNCH_SECOND_ACTIVITY) {
            requestTest()
        }
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest1") {
            var scheduledTestResponse =
                Gson().fromJson(response, ScheduledClass::class.java)
            if (scheduledTestResponse.PRACTICE.isNullOrEmpty()) {
                expandablePracticeTest.secondLayout.recyclerViewChild.visibility = View.GONE
                expandablePracticeTest.secondLayout.txtError.visibility = View.VISIBLE
            } else {
                expandablePracticeTest.secondLayout.recyclerViewChild.visibility =
                    View.VISIBLE
                expandablePracticeTest.secondLayout.txtError.visibility = View.GONE
                val completedList = db.getCompletedTest()
                completedList.forEachIndexed { _, completedListElement ->
                    attemptedTest!!.addAll(
                        scheduledTestResponse.PRACTICE.filter { it.testPaperId == completedListElement.testPaperId }
                            .toMutableList())
                    scheduledTestResponse.PRACTICE =
                        scheduledTestResponse.PRACTICE.filterNot { it.testPaperId == completedListElement.testPaperId }
                }
                val scheduledTestAdapter = ScheduledTestAdapter(
                    requireView().context,
                    scheduledTestResponse.PRACTICE,
                    this
                )
                expandablePracticeTest.secondLayout.recyclerViewChild.adapter =
                    scheduledTestAdapter
            }
            getAttemptedTest()
        } else if (responseCode == networkHelper.responseSuccess && tag == "getAttempted") {
            val attempted = Gson().fromJson(response, AttemptedResponse::class.java)
            if (attemptedTest?.size!! > 0) {
                for (attempt in attemptedTest!!) {
                    attempted.pRACTICE.add(
                        AttemptedTest(
                            "completed",
                            attempt.testPaperVo?.correctMark!!,
                            attempt.testPaperVo.duration,
                            attempt.expiryDate,
                            attempt.expiryDateTime,
                            attempt.expiryTime,
                            attempt.testPaperVo.name,
                            attempt.publishDate,
                            attempt.publishDateTime,
                            attempt.publishTime!!,
                            attempt.testPaperVo.questionCount,
                            attempt.testPaperVo.status,
                            "",
                            loginData.userDetail?.usersId!!,
                            loginData.userDetail?.userName!!,
                            attempt.testPaperVo.testCode,
                            attempt.testPaperId,
                            attempt.testPaperVo.testType,
                            attempt.testPaperVo.attempts,
                            attempt.testPaperVo.duration.toString(),
                            attempt.testPaperVo.correctMark.toString(),
                        )
                    )
                }
            }
            attemptedSetup(attempted)
        }
    }

    private fun attemptedSetup(attempted: AttemptedResponse) {

        Log.e("popAttempted", attempted.pRACTICE.toString())
        if (view != null) {

            if (attempted.pRACTICE.size > 0) {
                expandablePracticeTestAttempted.secondLayout.txtError.visibility = View.GONE
                val attemptedAdapter = AttemptedTestAdapter(
                    requireContext(),
                    attempted.pRACTICE.reversed(), this
                )
                expandablePracticeTestAttempted.secondLayout.recyclerViewChild.adapter = attemptedAdapter
                expandablePracticeTestAttempted.secondLayout.recyclerViewChild.visibility = View.VISIBLE

            } else {
                expandablePracticeTestAttempted.secondLayout.txtError.visibility = View.VISIBLE
            }
        }
    }

    private fun getAttemptedTest() {
        networkHelper.getCall(
            URLHelper.attemptedTests  + "?batchId=${
                loginData.userDetail?.batchList?.get(1)?.id
            }&studentId=${loginData.userDetail?.usersId}",
            "getAttempted",
            ApiUtils.getHeader(requireContext()),
            this
        )
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
            myPreferences.setBoolean(Define.TAKE_TEST_MODE_OFFLINE, false)
            goToTestScreen(mockTest)
            dialog.dismiss()
        }
        dialog.agree.setOnClickListener {
            myPreferences.setBoolean(Define.TAKE_TEST_MODE_OFFLINE, true)
            goToTestScreen(mockTest)
            dialog.dismiss()
        }
        dialog.show()
    }

    fun submitTest(jsonObject: JSONObject) {
        networkHelper.postCall(
            URLHelper.submitTestPaper ,
            jsonObject,
            "submitTestPaper",
            ApiUtils.getAuthorizationHeader(requireContext(), jsonObject.toString().length),
            this
        )
    }

    private fun goToTestScreen(mockTest: MOCKTEST) {
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        intent.putExtra("mockTest", mockTest)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(intent, Utils.LAUNCH_SECOND_ACTIVITY)
    }

    override fun onTestClicked(isClicked: Boolean, mockTest: MOCKTEST) {
        showDialog(mockTest)
    }

    override fun onResultClicked(id: String) {
        clickedTestPaperId = id
        val completedTest = db.getCompletedTest(id)

        val jsonAnsArray = JSONArray(completedTest.questionAnswerList!!)

        val jsonAnsObject = JSONObject()
        jsonAnsObject.put("testPaperId", completedTest.testPaperId)
        jsonAnsObject.put("attempt", completedTest.attempt)
        jsonAnsObject.put("studentId", completedTest.studentId)
        jsonAnsObject.put("testDurationTime", completedTest.testDurationTime)
        jsonAnsObject.put(
            "questionAnswerList",
            jsonAnsArray
        )
        submitTest(jsonAnsObject)
    }

    override fun onResultClicked(attempt: Int, studentId: String, testPaperId: String) {
    }

    override fun onReviewClicked(attempt: AttemptedTest) {
    }
}