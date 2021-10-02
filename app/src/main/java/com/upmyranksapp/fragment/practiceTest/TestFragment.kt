package com.upmyranksapp.fragment.practiceTest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.activity.AttemptedResultsActivity
import com.upmyranksapp.database.DatabaseHelper
import com.upmyranksapp.fragment.DoubtFragment
import com.upmyranksapp.model.onBoarding.AverageBatchTests
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.ApiUtils
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import com.upmyranksapp.utils.Utils
import kotlinx.android.synthetic.main.fragment_test.*
import org.json.JSONArray

private const val ARG_PARAM1 = "param1"

class TestFragment : Fragment(), OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    private var averBatchTest = mutableListOf<AverageBatchTests>()
    lateinit var myPreferences: MyPreferences
    lateinit var db: DatabaseHelper

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
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        averBatchTest = db.getAllAverageBatchTest()

        if (averBatchTest.isNullOrEmpty()) {
            requestSessions()
        } else {
            carouselView.adapter = CarouselAdapter(requireContext(), averBatchTest)
        }

        requestSessions()

        allResults.setOnClickListener {
            val intent = Intent(requireContext(), AttemptedResultsActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onStart() {
        super.onStart()
        childFragmentManager.beginTransaction()
            .replace(R.id.frameLayout1, TestTabFragment()).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> childFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout1, TestTabFragment()).commit()

                    1 -> childFragmentManager.beginTransaction()
                        .replace(R.id.frameLayout1, PracticeTabFragment.newInstance("", ""))
                        .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    private fun requestSessions() {

        val myBatchList = JSONArray()
        loginData.userDetail?.batchList?.forEach {
            myBatchList.put(it.id!!)
        }

        networkHelper.getCall(
            URLHelper.averageBatchTests + "?batchId=${
                loginData.userDetail?.batchList?.get(0)?.id
            }&studentId=${loginData.userDetail?.usersId.toString()}",
            "averageBatchTests",
            ApiUtils.getHeader(requireContext()),
            this
        )

    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "averageBatchTests") {
            val testResponse = Gson().fromJson(response, AverageBatchTests::class.java)
            db.saveAvg(testResponse)
            carouselView.adapter = CarouselAdapter(requireContext(), db.getAllAverageBatchTest())
        }
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
        fun newInstance(param1: Int) =
            DoubtFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_PARAM1, param1)
                }
            }
    }


}