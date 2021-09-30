package com.upmyranksapp.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.upmyranksapp.R
import com.upmyranksapp.adapter.CompletedLiveAdapter
import com.upmyranksapp.model.CompletedLiveItem
import com.upmyranksapp.model.onBoarding.CompletedSession
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.*
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_upcoming_live.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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


    fun getApiCall(context: Context) {
        RetroFitCall.retroFitCall()
        val service = RetroFitCall.retrofit.create(ApiInterface::class.java)
        val call = service.getData()
        call.enqueue(object : Callback<List<CompletedSession>> {
            override fun onResponse(
                call: Call<List<CompletedSession>>,
                response: Response<List<CompletedSession>>
            ) {
                if (response.code() == 200) {
                    var auditList : List<CompletedSession> = response.body()!!
                }

            }
            override fun onFailure(call: Call<List<CompletedSession>>, t: Throwable) {
                Toast.makeText(context, "failed", Toast.LENGTH_LONG).show()
            }
        })
    }


    private fun requestSessions() {

        val myBatchList = JSONArray()
        loginData.userDetail?.batchList?.forEach {
            myBatchList.put(it.id!!)
        }
        val myBranchIDs = JSONArray()
        loginData.userDetail?.branchList?.forEach {
            myBranchIDs.put(it.id!!)
        }


        val jsonObject = JSONObject()
        jsonObject.put("branchIds", myBranchIDs)
        jsonObject.put("coachingCentreId", loginData.userDetail?.coachingCenterId.toString())
        jsonObject.put("batchIds", myBatchList)


        stateful.showProgress()
        stateful.setProgressText("")
        networkHelper.postCallResponseArray(
            URLHelper.getCompletedSessionsSubject,
            jsonObject,
            "completedSessions",
            ApiUtils.getHeader(requireContext()),
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        try {
            stateful.showContent()
            if (responseCode == networkHelper.responseSuccess && tag == "completedSessions") {
                Log.e(
                    "soppers",
                    "responseCode: " + responseCode.toString() + "response: " + response + " tag: " + tag
                )
                  val arrayTutorialType = object : TypeToken<ArrayList<CompletedSession>>() {}.type
                  val liveItemResponse: ArrayList<CompletedSession> = Gson().fromJson(response, arrayTutorialType)
                  liveItemResponse.let {
                      val completedLiveAdapter = CompletedLiveAdapter(requireContext(), liveItemResponse)
                      recycler.adapter = completedLiveAdapter
                  }
            } else {
                showErrorMsg("No completed sessions available")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.ic_no_data)
        stateful.setOfflineRetryOnClickListener {
            requestSessions()
        }
    }
}