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
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.ChapterActivity
import com.trisys.rn.baseapp.adapter.CourseAdapter
import com.trisys.rn.baseapp.adapter.SubjectClickListener
import com.trisys.rn.baseapp.adapter.SubjectsAdapter
import com.trisys.rn.baseapp.fragment.practiceTest.CourseListener
import com.trisys.rn.baseapp.model.CourseResponse
import com.trisys.rn.baseapp.model.Datum
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_learn.*


class LearnFragment : Fragment(), SubjectClickListener, CourseListener, OnNetworkResponse {

    private lateinit var courseRecycler: RecyclerView
    private var loginData = LoginData()
    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper
    var batchIds : String? = null

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
        return inflater.inflate(R.layout.fragment_learn, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        courseRecycler = view.findViewById(R.id.recyclerviewcourse) as RecyclerView

        courseCall()
        requestSessions(loginData.userDetail?.batchList?.get(0)?.courseId!!)
    }

    private fun subjectCall(subjectList: ArrayList<Datum>) {
        val manager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW)
        manager.justifyContent = JustifyContent.CENTER
        recyclerview.layoutManager = manager
        if (subjectList.size > 0) {
            //adding a layoutmanager
            val adapter = SubjectsAdapter(requireContext(), subjectList, batchIds.toString(),this)

            //now adding the adapter to recyclerview
            recyclerview.adapter = adapter
        } else {
            showErrorMsg("No subject found.")
        }
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
        val adapter = CourseAdapter(requireContext(), this, loginData.userDetail?.batchList!!)
        courseRecycler.adapter = adapter
    }

    private fun requestSessions(batchId: String) {
        networkHelper.getCall(
            URLHelper.courseURL + batchId,
            "getCourse",
            ApiUtils.getHeader(requireContext()),
            this
        )

    }

    override fun onSubjectClicked(batchId: String,id: String, title: String) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        intent.putExtra("id", batchId)
        intent.putExtra("batchId", id)
        intent.putExtra("title", title)
        startActivity(intent)
    }


    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "getCourse") {
            val courseResponse = Gson().fromJson(response, CourseResponse::class.java)
            subjectCall(courseResponse.data!!)
        } else {
            showErrorMsg(requireActivity().getString(R.string.sfl_default_error))
        }
    }

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.icon_error)
        stateful.setOfflineRetryOnClickListener {
            requestSessions(loginData.userDetail?.batchList?.get(0)?.courseId!!)
        }
    }

    override fun onCourseClicked(batchId: String, id: String, position: Int) {
        courseRecycler.layoutManager?.scrollToPosition(position)
        batchIds = id
        requestSessions(batchId)
    }
}