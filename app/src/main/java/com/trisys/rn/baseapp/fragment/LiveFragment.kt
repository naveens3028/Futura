package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.StudyAdapter
import com.trisys.rn.baseapp.model.StudyItem
import com.trisys.rn.baseapp.model.UpcomingLiveItem
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.RequestType
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.URLHelper.getSessions
import com.trisys.rn.baseapp.utils.UrlConstants.kPREVIOUS
import kotlinx.android.synthetic.main.fragment_live.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LiveFragment : Fragment(), OnNetworkResponse {

    private var upcomingLiveList = ArrayList<UpcomingLiveItem>()
    private var studyList = ArrayList<StudyItem>()
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
        return inflater.inflate(R.layout.fragment_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        requestSessions()

        upcomingLiveList.add(UpcomingLiveItem("Mathematics", R.drawable.mathematics))
        upcomingLiveList.add(UpcomingLiveItem("Physics", R.drawable.mathematics))
        upcomingLiveList.add(UpcomingLiveItem("Chemistry", R.drawable.mathematics))
        upcomingLiveList.add(UpcomingLiveItem("Biology", R.drawable.mathematics))


        //Sample Data
        studyList.add(
            StudyItem(
                "Mathematics",
                "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.caribbean_green
            )
        )
        studyList.add(
            StudyItem(
                "Physics", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.blue_violet_crayola
            )
        )
        studyList.add(
            StudyItem(
                "Chemistry", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.safety_yellow
            )
        )
        studyList.add(
            StudyItem(
                "Biology", "L2 - Functions and Binary Operations",
                "4 Of 8 Lesson", R.drawable.mathematics, 50, R.color.light_coral
            )
        )

        val studyRecyclerView = view.findViewById(R.id.studyRecycler) as RecyclerView
        val studyAdapter = StudyAdapter(requireContext(), studyList)
        studyRecyclerView.adapter = studyAdapter

    }

    override fun onStart() {
        super.onStart()
        childFragmentManager.beginTransaction()
            .replace(R.id.liveFrameLayout, UpcomingLiveFragment.newInstance("", "")).commit()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 ->
                        childFragmentManager.beginTransaction()
                            .replace(R.id.liveFrameLayout, UpcomingLiveFragment.newInstance("", ""))
                            .commit()
                    1 ->
                        childFragmentManager.beginTransaction()
                            .replace(R.id.liveFrameLayout, UpcomingLiveFragment.newInstance("", ""))
                            .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

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
            LiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestSessions() {

        val params = HashMap<String, String>()
        params["branchIds"] = loginData.userDetail?.branchIds.toString()
        params["coachingCentreId"] = loginData.userDetail?.coachingCenterId.toString()
        params["batchIds"] = loginData.userDetail?.batchIds.toString()
        params["sessionTense"] = kPREVIOUS

        networkHelper.call(
            RequestType.POST_WITH_AUTH,
            getSessions,
            params,
            "getSessions",
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "getSessions") {
            Toast.makeText(requireContext(), "login successful", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(requireContext(), "login Failed", Toast.LENGTH_LONG).show()
        }
    }
}  