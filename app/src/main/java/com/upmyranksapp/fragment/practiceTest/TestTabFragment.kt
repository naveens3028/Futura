package com.upmyranksapp.fragment.practiceTest

import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.activity.TakeTestActivity
import com.upmyranksapp.activity.TestResultActivity
import com.upmyranksapp.adapter.ScheduledTestAdapter
import com.upmyranksapp.adapter.SubjectsAdapter
import com.upmyranksapp.adapter.TestClickListener
import com.upmyranksapp.adapter.test.AttemptedTestAdapter
import com.upmyranksapp.adapter.test.TestGroupingAdapter
import com.upmyranksapp.adapter.test.UnAttemptedTestAdapter
import com.upmyranksapp.database.DatabaseHelper
import com.upmyranksapp.model.*
import com.upmyranksapp.model.onBoarding.AttemptedResponse
import com.upmyranksapp.model.onBoarding.AttemptedTest
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.model.onBoarding.UnAttempted
import com.upmyranksapp.network.ApiUtils
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.practiceTest.TestReviewActivity
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.DialogUtils
import com.upmyranksapp.utils.MyPreferences
import com.upmyranksapp.utils.Utils
import kotlinx.android.synthetic.main.dialog_confirm_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.close
import kotlinx.android.synthetic.main.fragment_practice_tab.*
import kotlinx.android.synthetic.main.fragment_test_tab.*
import kotlinx.android.synthetic.main.test_layout_child.view.*
import kotlinx.android.synthetic.main.test_layout_parent.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONArray
import org.json.JSONObject


class TestTabFragment : Fragment(), TestClickListener, OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    lateinit var db: DatabaseHelper
    lateinit var dialogUtils: DialogUtils
    lateinit var testPaperId: String
    lateinit var attempt1: AttemptedTest
    var attemptedTest: ArrayList<MOCKTEST>? = ArrayList()
    var clickedTestPaperId = ""
    var batchId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
        db = DatabaseHelper(requireContext())
        dialogUtils = DialogUtils()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test_tab, container, false)

    }

    private fun requestTest(batchId: String) {

        val nightModeFlags: Int = requireContext().resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)
        when (nightModeFlags) {

            Configuration.UI_MODE_NIGHT_NO -> {

            }
            Configuration.UI_MODE_NIGHT_UNDEFINED -> {

            }
        }

        val params = HashMap<String, String>()
        params["batchId"] = loginData.userDetail?.batchList?.get(0)?.id.toString()

        params["studentId"] = loginData.userDetail?.usersId.toString()


        val params1 = HashMap<String, String>()
        params1["attempt"] = "attempt"
        params1["studentId"] = "5085517d-90af-49ae-b95b-68c7ec234363"
        params1["testPaperId"] = "8571b238-3628-4935-b233-b2d8cba32865"

        networkHelper.call(
            networkHelper.GET,
            networkHelper.RESTYPE_OBJECT,
            URLHelper.unattemptedTests,
            params,
            Priority.HIGH,
            "getUnAttempted",
            this
        )


        networkHelper.call(
            networkHelper.POST,
            networkHelper.RESTYPE_OBJECT,
            URLHelper.answeredTestPapers,
            params1,
            Priority.HIGH,
            "getAnsweredTestResult",
            this
        )


        networkHelper.getCall(
            URLHelper.scheduleTestsForStudent + "?batchId=${
                loginData.userDetail?.batchList?.get(1)?.id
            }&studentId=${loginData.userDetail?.usersId}",
            "scheduledTest",
            ApiUtils.getHeader(requireContext()),
            this
        )

    }

    private fun getAttemptedTest(batchId : String) {
        val params = HashMap<String, String>()
        params["batchId"] = loginData.userDetail?.batchList?.get(0)?.id.toString()

        params["studentId"] = loginData.userDetail?.usersId.toString()

        networkHelper.call(
            networkHelper.GET,
            networkHelper.RESTYPE_OBJECT,
            URLHelper.attemptedTests,
            params,
            Priority.HIGH,
            "getAttempted",
            this
        )
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

    private fun goToTestScreen(mockTest: MOCKTEST) {
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        intent.putExtra("mockTest", mockTest)
        /*intent.putExtra("duration", mockTest.testPaperVo?.duration)
        intent.putExtra("timeLeft", mockTest.testPaperVo?.timeLeft)
        intent.putExtra("questionCount", mockTest.testPaperVo?.questionCount.toString())
        intent.putExtra("noAttempted", mockTest.testPaperVo?.attempts.toString())
        intent.putExtra("date", Utils.getDateValue(mockTest.publishDateTime))
        intent.putExtra("testPaperId", mockTest.testPaperId)
        intent.putExtra("testPaperName", mockTest.testPaperVo?.name)
        intent.putExtra("isPauseAllow", mockTest.testPaperVo?.isPauseAllow)*/
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivityForResult(intent, Utils.LAUNCH_SECOND_ACTIVITY)
    }

    override fun onStart() {
        super.onStart()
        expandableScheduleTest.parentLayout.txtParentTitle.text = "Scheduled Test"
        expandableAttemptedTest.parentLayout.txtParentTitle.text = "Attempted Test"
        expandableUnAttemptedTest.parentLayout.txtParentTitle.text = "Un Attempted Test"

        expandableScheduleTest.secondLayout.txtError.text = "No Test Found in Scheduled Test"
        expandableAttemptedTest.secondLayout.txtError.text = "No Test Found in Attempted Test"
        expandableUnAttemptedTest.secondLayout.txtError.text = "No Test Found in Un Attempted Test"

        expandableScheduleTest.parentLayout.setOnClickListener {
            if(expandableScheduleTest.isExpanded)
            expandableScheduleTest.collapse()
            else {
                expandableScheduleTest.expand()
                expandableAttemptedTest.collapse()
                expandableUnAttemptedTest.collapse()
            }
        }
        expandableAttemptedTest.parentLayout.setOnClickListener {
            if(expandableAttemptedTest.isExpanded)
                expandableAttemptedTest.collapse()
            else {
                expandableAttemptedTest.expand()
                expandableScheduleTest.collapse()
                expandableUnAttemptedTest.collapse()
            }
        }
        expandableUnAttemptedTest.parentLayout.setOnClickListener {
            if(expandableUnAttemptedTest.isExpanded)
                expandableUnAttemptedTest.collapse()
            else {
                expandableUnAttemptedTest.expand()
                expandableScheduleTest.collapse()
                expandableAttemptedTest.collapse()
            }
        }

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        requestTest(batchId)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.LAUNCH_SECOND_ACTIVITY) {
            requestTest(batchId)
        }
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

    fun submitTest(jsonObject: JSONObject) {
        networkHelper.postCall(
            URLHelper.submitTestPaper,
            jsonObject,
            "submitTestPaper",
            ApiUtils.getAuthorizationHeader(requireContext(), jsonObject.toString().length),
            this
        )
    }

    override fun onResultClicked(attempt: Int, studentId: String, testPaperID: String) {
        testPaperId = testPaperID
        val intent = Intent(requireContext(), TestResultActivity::class.java)
        intent.putExtra("attempt", attempt)
        intent.putExtra("studentId", studentId)
        intent.putExtra("testPaperId", testPaperID)
        startActivity(intent)

    }

    override fun onReviewClicked(attempt: AttemptedTest) {
        testPaperId = attempt.testPaperId
        attempt1 = attempt
        val intent = Intent(context, TestReviewActivity::class.java)
        intent.putExtra("AttemptedTest", attempt)
        startActivity(intent)
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        try {
            if(requireActivity() != null) {
                if (responseCode == networkHelper.responseSuccess && tag == "getUnAttempted") {
                    val unAttempted = Gson().fromJson(response, UnAttempted::class.java)
                    unAttemptedSetup(unAttempted)
                } else if (responseCode == networkHelper.responseSuccess && tag == "getAttempted") {
                    val attempted = Gson().fromJson(response, AttemptedResponse::class.java)
                    if (attemptedTest?.size!! > 0) {
                        for (attempt in attemptedTest!!) {
                            attempted.mOCKTEST.add(
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
                } else if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {
                    val scheduledTestResponse =
                        Gson().fromJson(response, ScheduledClass::class.java)


                    if (scheduledTestResponse.MOCK_TEST.isNullOrEmpty()) {
                        expandableScheduleTest.secondLayout.recyclerViewChild.visibility = View.GONE
                        expandableScheduleTest.secondLayout.txtError.visibility = View.VISIBLE
                    } else {
                        expandableScheduleTest.secondLayout.recyclerViewChild.visibility =
                            View.VISIBLE
                        expandableScheduleTest.secondLayout.txtError.visibility = View.GONE
                        val completedList = db.getCompletedTest()
                        completedList.forEachIndexed { _, completedListElement ->
                            attemptedTest!!.addAll(
                                scheduledTestResponse.MOCK_TEST.filter { it.testPaperId == completedListElement.testPaperId }
                                    .toMutableList())
                            scheduledTestResponse.MOCK_TEST =
                                scheduledTestResponse.MOCK_TEST.filterNot { it.testPaperId == completedListElement.testPaperId }
                        }
                        val scheduledTestAdapter = ScheduledTestAdapter(
                            requireView().context,
                            scheduledTestResponse.MOCK_TEST,
                            this
                        )
                        expandableScheduleTest.secondLayout.recyclerViewChild.adapter =
                            scheduledTestAdapter
                    }
                    getAttemptedTest(batchId)
                } else if (responseCode == networkHelper.responseSuccess && tag == "submitTestPaper") {
                    val submittedResult = Gson().fromJson(response, SubmittedResult::class.java)
                    db.deleteTest(submittedResult.testPaperId)
                    attemptedTest =
                        attemptedTest?.filterNot { it.testPaperId == submittedResult.testPaperId } as ArrayList<MOCKTEST>?
                    getAttemptedTest(batchId)
                } else if (responseCode == networkHelper.responseSuccess && tag == "answeredTestPapersResult") {
                    dialogUtils.dismissLoader()
                    val testResponseResult = Gson().fromJson(response, TestResultsModel::class.java)
                    testResponseResult.testPaperId = testPaperId
                    db.saveResult(testResponseResult)
                    val intent = Intent(requireContext(), TestResultActivity::class.java)
                    intent.putExtra("testPaperId", testPaperId)
                    startActivity(intent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun unAttemptedSetup(unAttempted: UnAttempted) {
        if (view != null) {
            if (unAttempted.mockTest != null) {
                if (unAttempted.mockTest!!.isNotEmpty()) {
                    expandableUnAttemptedTest.secondLayout.txtError.visibility = View.GONE
                    val unattemptedAdapter = UnAttemptedTestAdapter(
                        requireContext(),
                        unAttempted.mockTest!!, this
                    )
                    expandableUnAttemptedTest.secondLayout.recyclerViewChild.adapter = unattemptedAdapter
                } else {
                    expandableUnAttemptedTest.secondLayout.txtError.visibility = View.VISIBLE
                }
            }
        }else{
            expandableUnAttemptedTest.secondLayout.txtError.visibility = View.VISIBLE
        }
    }

    private fun attemptedSetup(attempted: AttemptedResponse) {
        if (view != null) {
            if(attempted.mOCKTEST.size > 0) {
                expandableAttemptedTest.secondLayout.txtError.visibility = View.GONE
                expandableAttemptedTest.secondLayout.recyclerViewChild.visibility = View.VISIBLE
                val attemptedAdapter = AttemptedTestAdapter(
                    requireContext(),
                    attempted.mOCKTEST.reversed(), this
                )
                expandableAttemptedTest.secondLayout.recyclerViewChild.adapter = attemptedAdapter
            }else {
                expandableAttemptedTest.secondLayout.txtError.visibility = View.VISIBLE
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PerformanceFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestTabFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onMessageEvent(event: OnEventData?) {
//        val data = loginData.userDetail?.batchList?.get(event?.batchPosition!!)
//        batchId = data?.id!!
//        requestTest(data?.id!!)
//    }

//    private fun subjectCall(subjectList: ArrayList<String>) {
//        if (subjectList.size > 0) {
//            val adapter = TestGroupingAdapter(requireContext(), subjectList)
//            //now adding the adapter to recyclerview
//            testRecycler1.adapter = adapter
//        } else {
//            txtError.visibility = View.VISIBLE
//        }
//    }


    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}