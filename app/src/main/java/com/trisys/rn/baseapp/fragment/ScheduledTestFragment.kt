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
import com.trisys.rn.baseapp.adapter.HomeStudyAdapter
import com.trisys.rn.baseapp.adapter.ScheduledTestAdapter
import com.trisys.rn.baseapp.adapter.TestClickListener
import com.trisys.rn.baseapp.model.LiveResponse
import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.ScheduledTestClass
import com.trisys.rn.baseapp.model.ScheduledTestItem
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.*
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_live.*
import kotlinx.android.synthetic.main.fragment_upcoming_live.*
import org.json.JSONException
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
class ScheduledTestFragment : Fragment(), TestClickListener,OnNetworkResponse {

    private var scheduledTestList = ArrayList<ScheduledTestItem>()
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
        scheduledTestList.add(
            ScheduledTestItem(
                "JEE Mains Test2",
                "17th Mar, 9:30AM",
                "120",
                "1h 25m",
                R.color.caribbean_green
            )
        )
        scheduledTestList.add(
            ScheduledTestItem(
                "NEET Test2",
                "17th Mar, 9:30AM",
                "120", "1h 25m",
                R.color.light_coral
            )
        )
        scheduledTestList.add(
            ScheduledTestItem(
                "NCERT Test2",
                "19th Mar, 9:30AM",
                "120", "1h 25m",
                R.color.blue_violet_crayola
            )
        )
        scheduledTestList.add(ScheduledTestItem("JEE Mains Test3", "19th Mar, 9:30AM", "120", "1h 25m", R.color.caribbean_green))
        requestSessions()
    }

    override fun onTestClicked(isClicked: Boolean) {
        val intent = Intent(requireContext(), TakeTestActivity::class.java)
        startActivity(intent)
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
            ScheduledTestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestSessions() {

        networkHelper.getCall(
            URLHelper.scheduleTestsForStudent + "?batchId=${
                loginData.userDetail?.branchIds?.get(0)}&studentId=${loginData.userDetail?.usersId}",
            "scheduledTest",
            ApiUtils.getHeader(requireContext()),
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        val scheduledTestResponse = Gson().fromJson(response, ScheduledTestClass::class.java)
        val completedLiveAdapter = ScheduledTestAdapter(requireContext(),scheduledTestList, scheduledTestResponse.mOCKTEST, this)
        recycler.adapter = completedLiveAdapter
        if (responseCode == networkHelper.responseSuccess && tag == "scheduledTest") {

        }else{
            Toast.makeText(requireContext(), "Data unable to load", Toast.LENGTH_LONG).show()
        }

    }
}