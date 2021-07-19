package com.trisys.rn.baseapp.fragment.practiceTest

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.TakeTestActivity
import com.trisys.rn.baseapp.activity.TestResultActivity
import com.trisys.rn.baseapp.adapter.ScheduledTestAdapter
import com.trisys.rn.baseapp.adapter.TestClickListener
import com.trisys.rn.baseapp.adapter.test.AttemptedTestAdapter
import com.trisys.rn.baseapp.adapter.test.UnAttemptedTestAdapter
import com.trisys.rn.baseapp.database.DatabaseHelper
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.ScheduledClass
import com.trisys.rn.baseapp.model.onBoarding.AttemptedResponse
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.model.onBoarding.UnAttempted
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.dialog_confirm_test.*
import kotlinx.android.synthetic.main.dialog_jump_to_questions.close
import kotlinx.android.synthetic.main.fragment_test_tab.*

class TestTabFragment : Fragment(), TestClickListener, OnNetworkResponse {

    private var checkVisible: Boolean? = false
    private var unAttemptedIsVisible: Boolean = false
    private var attemptedIsVisible: Boolean = false
    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    lateinit var db: DatabaseHelper
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
        return inflater.inflate(R.layout.fragment_test_tab, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        requestTest()

        arrowscheduled.rotation = arrowscheduled.rotation + 180

        arrowscheduled.setOnClickListener {
            if (checkVisible == false) {
                arrowscheduled.rotation = arrowscheduled.rotation + 180
                scheduleTestRecyclerView.visibility = View.GONE
                noTest.visibility = View.GONE
                checkVisible = true
            } else {
                arrowscheduled.rotation = arrowscheduled.rotation + 180
                scheduleTestRecyclerView.visibility = View.VISIBLE
                noTest.visibility = View.VISIBLE
                checkVisible = false
            }
        }

        arrowsUnAttempted.setOnClickListener {
            if (!unAttemptedIsVisible) {
                arrowsUnAttempted.rotation = arrowsUnAttempted.rotation + 180
                unattemptedTestRecyclerView.visibility = View.VISIBLE
                unAttemptedIsVisible = true
            } else {
                arrowsUnAttempted.rotation = arrowsUnAttempted.rotation + 180
                unattemptedTestRecyclerView.visibility = View.GONE
                unAttemptedIsVisible = false
            }
        }

        arrowsAttempted.setOnClickListener {
            if (!attemptedIsVisible) {
                arrowsAttempted.rotation = arrowsAttempted.rotation + 180
                attemptedTestRecyclerView.visibility = View.VISIBLE
                attemptedIsVisible = true
            } else {
                arrowsAttempted.rotation = arrowsAttempted.rotation + 180
                attemptedTestRecyclerView.visibility = View.GONE
                attemptedIsVisible = false
            }
        }
    }

    private fun requestTest() {

        val params = HashMap<String, String>()
        params["batchId"] = loginData.userDetail?.batchIds?.get(0).toString()
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
            networkHelper.GET,
            networkHelper.RESTYPE_OBJECT,
            URLHelper.attemptedTests,
            params,
            Priority.HIGH,
            "getAttempted",
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
                loginData.userDetail?.batchIds?.get(0)
            }&studentId=${loginData.userDetail?.usersId}",
            "scheduledTest",
            ApiUtils.getHeader(requireContext()),
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
        startActivityForResult(intent, Utils.LAUNCH_SECOND_ACTIVITY)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Utils.LAUNCH_SECOND_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                requestTest()
            }
        }
    }

    override fun onResultClicked(isClicked: Boolean) {

    }

    override fun onResultClicked(attempt: Int, studentId: String, testPaperId: String) {
        val intent = Intent(requireContext(), TestResultActivity::class.java)
        intent.putExtra("attempt", attempt)
        intent.putExtra("studentId", studentId)
        intent.putExtra("testPaperId", testPaperId)
        startActivity(intent)
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        try {
            if (responseCode == networkHelper.responseSuccess && tag == "getUnAttempted") {
                val unAttempted = Gson().fromJson(response, UnAttempted::class.java)
                unAttemptedSetup(unAttempted)
            } else if (responseCode == networkHelper.responseSuccess && tag == "getAttempted") {
                val attempted = Gson().fromJson(response, AttemptedResponse::class.java)
                attemptedSetup(attempted)
            } else if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {
                val scheduledTestResponse =
                    Gson().fromJson(response, ScheduledClass::class.java)
                if (scheduledTestResponse.MOCK_TEST.isNullOrEmpty()) {
                    scheduleTestRecyclerView.visibility = View.GONE
                    noTest.visibility = View.VISIBLE
                } else {
                    scheduleTestRecyclerView.visibility = View.VISIBLE
                    noTest.visibility = View.GONE
                    val scheduledTestAdapter = ScheduledTestAdapter(
                        requireView().context,
                        scheduledTestResponse.MOCK_TEST,
                        this
                    )
                    scheduleTestRecyclerView.adapter = scheduledTestAdapter
                }

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun unAttemptedSetup(unAttempted: UnAttempted) {
        if (view != null) {
            val unattemptedAdapter = UnAttemptedTestAdapter(
                requireContext(),
                unAttempted.mockTest!!, this
            )
            unattemptedTestRecyclerView.adapter = unattemptedAdapter
        }
    }

    private fun attemptedSetup(attempted: AttemptedResponse) {
        if (view != null) {
            val attemptedAdapter = AttemptedTestAdapter(
                requireContext(),
                attempted.mOCKTEST, this
            )
            attemptedTestRecyclerView.adapter = attemptedAdapter
        }
    }
}