package com.trisys.rn.baseapp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.androidnetworking.common.Priority
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.TakeResultActivity
import com.trisys.rn.baseapp.activity.TakeTestActivity
import com.trisys.rn.baseapp.adapter.ScheduledTestAdapter
import com.trisys.rn.baseapp.adapter.TestClickListener
import com.trisys.rn.baseapp.adapter.test.AttemptedTestAdapter
import com.trisys.rn.baseapp.adapter.test.UnAttemptedTestAdapter
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.ScheduledTestClass
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
import kotlinx.android.synthetic.main.fragment_scheduled_test.*
import kotlinx.android.synthetic.main.fragment_test_tab.*
import kotlinx.android.synthetic.main.fragment_upcoming_live.recycler

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TestTabFragment : Fragment(), TestClickListener, OnNetworkResponse {

    private var checkVisible: Boolean? = false
    private var unAttemptedIsVisible: Boolean = false
    private var attemptedIsVisible: Boolean = false
    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
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
                checkVisible = true
            } else {
                arrowscheduled.rotation = arrowscheduled.rotation + 180
                scheduleTestRecyclerView.visibility = View.VISIBLE
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
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        intent.putExtra("duration", Utils.getDuration(mockTest.testPaperVo.duration))
        intent.putExtra("questionCount", mockTest.testPaperVo.questionCount.toString())
        intent.putExtra("noAttempted", mockTest.testPaperVo.attempts.toString())
        intent.putExtra("date", Utils.getDateValue(mockTest.publishDateTime))
        intent.putExtra("testPaperId", mockTest.testPaperId)
        intent.putExtra("testPaperName", mockTest.testPaperVo.name)
        startActivity(intent)
    }

    override fun onResultClicked(isClicked: Boolean) {
        val intent = Intent(requireContext(), TakeResultActivity::class.java)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestTabFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        Log.e("poppers", "response: $response  tags: $tag  responseCode: $responseCode.toString()")
        if (tag == "getUnAttempted" && responseCode == networkHelper.responseSuccess) {
            val unAttempted = Gson().fromJson(response, UnAttempted::class.java)
            unAttemptedSetup(unAttempted)
        } else if (responseCode == networkHelper.responseSuccess) {
            val attempted = Gson().fromJson(response, AttemptedResponse::class.java)
            attemptedSetup(attempted)
        }
        if (view != null) {
            if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {
                val scheduledTestResponse =
                    Gson().fromJson(response, ScheduledTestClass::class.java)
                if (scheduledTestResponse.mOCKTEST.isNotEmpty()) {
                    val scheduledTestAdapter = ScheduledTestAdapter(
                        requireView().context,
                        scheduledTestResponse.mOCKTEST,
                        this
                    )
                    scheduleTestRecyclerView.adapter = scheduledTestAdapter
                } else {
                    recycler.visibility = View.GONE
                    noData.visibility = View.VISIBLE
                }
            } else {
//                Toast.makeText(requireContext(), "Data unable to load", Toast.LENGTH_LONG).show()
            }
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