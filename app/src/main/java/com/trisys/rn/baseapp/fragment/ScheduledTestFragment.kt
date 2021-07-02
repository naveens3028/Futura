package com.trisys.rn.baseapp.fragment

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
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.ScheduledTestClass
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ScheduledTestFragment : Fragment(), TestClickListener, OnNetworkResponse {

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
        return inflater.inflate(R.layout.fragment_scheduled_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        requestTest()
    }

    override fun onTestClicked(isClicked: Boolean,mockTest: MOCKTEST) {
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        intent.putExtra("duration", getDuration(mockTest.testPaperVo.duration))
        intent.putExtra("questionCount", mockTest.testPaperVo.questionCount.toString())
        intent.putExtra("noAttempted", mockTest.testPaperVo.attempts.toString())
        intent.putExtra("date", getDateValue(mockTest.publishDateTime))
        intent.putExtra("testPaperId", mockTest.testPaperId)
        intent.putExtra("testPaperName", mockTest.testPaperVo.name)
        startActivity(intent)
    }

    override fun onResultClicked(isClicked: Boolean) {

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ScheduledTestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
            if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {
                val scheduledTestResponse =
                    Gson().fromJson(response, ScheduledTestClass::class.java)
                if (scheduledTestResponse.mOCKTEST.isNotEmpty()) {
                    val scheduledTestAdapter = ScheduledTestAdapter(
                        requireView().context,
                        scheduledTestResponse.mOCKTEST,
                        this
                    )
                    recycler.adapter = scheduledTestAdapter
                } else {
                    recycler.visibility = View.GONE
                    noData.visibility = View.VISIBLE
                }
            } else {
//                Toast.makeText(requireContext(), "Data unable to load", Toast.LENGTH_LONG).show()
            }
        }
    }

}
