package com.trisys.rn.baseapp.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.CompletedLiveItem
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import org.json.JSONArray
import org.json.JSONObject

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class CompletedLiveFragment : Fragment(), OnNetworkResponse {

    private var completedLiveList = ArrayList<CompletedLiveItem>()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    private var loginData = LoginData()

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
        return inflater.inflate(R.layout.fragment_upcoming_live, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        completedLiveList.add(
            CompletedLiveItem(
                "Chemistry",
                "",
                "L2 -Chemical Reaction and Periodic Table",
                R.color.safety_yellow
            )
        )
        completedLiveList.add(
            CompletedLiveItem(
                "Physics",
                "",
                "L3 - Universe, Galaxy and Solar System",
                R.color.blue_violet_crayola
            )
        )
        completedLiveList.add(
            CompletedLiveItem(
                "Biology",
                "",
                "L4 - Digestive System and Human Brain",
                R.color.light_coral
            )
        )
        completedLiveList.add(
            CompletedLiveItem(
                "Mathematics",
                "",
                "L5 - Trigonometry and Functions",
                R.color.caribbean_green
            )
        )
        Log.e("completed", "success")
        requestSessions()

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
            CompletedLiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestSessions() {

        val jsonObject = JSONObject()
        jsonObject.put("branchIds", JSONArray(loginData.userDetail?.branchIds))
        jsonObject.put("coachingCentreId", loginData.userDetail?.coachingCenterId.toString())
        jsonObject.put("batchIds", JSONArray(loginData.userDetail?.batchIds))

        networkHelper.postCallResponseArray(
            URLHelper.getCompletedSessionsSubject,
            jsonObject,
            "completedSessions",
            ApiUtils.getAuthorizationHeader(requireContext(), jsonObject.toString().length),
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "completedSessions") {
            Log.e(
                "soppers",
                "responseCode: " + responseCode.toString() + "response: " + response + " tag: " + tag
            )
            /*  val arrayTutorialType = object : TypeToken<ArrayList<CompletedSession>>() {}.type
              val liveItemResponse: ArrayList<CompletedSession> = Gson().fromJson(response, arrayTutorialType)
              liveItemResponse.let {
                  val completedLiveAdapter = CompletedLiveAdapter(requireContext(), completedLiveList,liveItemResponse, true)
                  recycler.adapter = completedLiveAdapter
              }*/
        } else {
            Toast.makeText(requireContext(), "No completed sessions available", Toast.LENGTH_SHORT)
                .show()
        }
    }
}