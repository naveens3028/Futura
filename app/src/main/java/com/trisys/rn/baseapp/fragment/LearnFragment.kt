package com.trisys.rn.baseapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.ChapterActivity
import com.trisys.rn.baseapp.adapter.CourseAdapter
import com.trisys.rn.baseapp.adapter.SubjectClickListener
import com.trisys.rn.baseapp.adapter.SubjectsAdapter
import com.trisys.rn.baseapp.fragment.Test.CourseListener
import com.trisys.rn.baseapp.model.CourseResponse
import com.trisys.rn.baseapp.model.Datum
import com.trisys.rn.baseapp.model.Subjects
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LearnFragment : Fragment(), SubjectClickListener, CourseListener, OnNetworkResponse {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var subjectRecycler: RecyclerView
    private lateinit var courseRecycler: RecyclerView
    private var subjectList = ArrayList<Subjects>()
    private var courseList = ArrayList<String>()
    private var loginData = LoginData()
    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        subjectRecycler = view.findViewById(R.id.recyclerview) as RecyclerView
        courseRecycler = view.findViewById(R.id.recyclerviewcourse) as RecyclerView

        subjectList.add(Subjects("Physics", R.drawable.physics))
        subjectList.add(Subjects("Chemistry", R.drawable.chemistry))
        subjectList.add(Subjects("Biology", R.drawable.biology))
        subjectList.add(Subjects("Mathematics", R.drawable.maths))

        courseList.add("NCERT")
        courseList.add("NEET")
        courseList.add("JEE MAINS")

        courseCall()
        requestSessions(loginData.userDetail?.batchList?.get(0)?.courseId!!)
    }

    private fun subjectCall(subjectList: ArrayList<Datum>) {
        //adding a layoutmanager
        val adapter = SubjectsAdapter(requireContext(), subjectList, this)

        //now adding the adapter to recyclerview
        subjectRecycler.adapter = adapter
    }

    @SuppressLint("WrongConstant")
    private fun courseCall() {
        courseRecycler.layoutManager = LinearLayoutManager(context, LinearLayout.HORIZONTAL, false)

        courseRecycler.addItemDecoration(
            DividerItemDecoration(
                context,
                LinearLayoutManager.HORIZONTAL
            )
        )

        val adapter = CourseAdapter(requireContext(),this, loginData.userDetail?.batchList!!)

        //now adding the adapter to recyclerview
        courseRecycler.adapter = adapter
    }

    private fun requestSessions(batchId : String) {

        networkHelper.getCall(
            URLHelper.courseURL + batchId,
            "scheduledTest",
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
            LearnFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onSubjectClicked(isClicked: Boolean) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        startActivity(intent)
    }

    override fun onCoureClicked(batchId: String) {
        requestSessions(batchId)
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        Log.e("naveen", "responseCode: " +responseCode.toString() + "response: " + response + "tag" + tag)
        val courseResponse = Gson().fromJson(response, CourseResponse::class.java)
       subjectCall(courseResponse.data!!)
    }

}