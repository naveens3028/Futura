package com.upmyranksapp.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.activity.ChapterActivity
import com.upmyranksapp.activity.TestVideoActivity
import com.upmyranksapp.adapter.SubjectClickListener
import com.upmyranksapp.adapter.SubjectsAdapter
import com.upmyranksapp.model.CourseResponse
import com.upmyranksapp.model.Datum
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_common_learn.*
import java.util.HashMap
import kotlin.math.log

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class FragmentLearnCommon : Fragment(), SubjectClickListener, OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper
    var batchIds: String? = null
    var courseId: String? = null

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
        return inflater.inflate(R.layout.fragment_common_learn, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        batchIds =  if (loginData.userDetail?.batchList!![0].additionalCourseId.isNullOrEmpty() && loginData.userDetail!!.userName != "QR1001") {
            loginData.userDetail?.batchList!![0].id
        } else if (loginData.userDetail!!.userName == "QR1001") {
            if (loginData.userDetail?.batchList!![1].additionalCourseId.isNullOrEmpty()){
                loginData.userDetail?.batchList!![1].id
            }else{
                loginData.userDetail?.batchList!![1].additionalCourseId
            }
        }else {
            loginData.userDetail?.batchList!![0].additionalCourseId
        }

    }


    override fun onResume() {
        super.onResume()
        arguments?.let {
            courseId = it.getString(ARG_PARAM2)
        }
        if (!courseId.isNullOrEmpty()){
            requestSessions(courseId!!)
        } else if (loginData.userDetail?.batchList?.get(0)?.additionalCourseId.isNullOrEmpty()) {
            requestSessions(loginData.userDetail?.batchList?.get(0)?.courseId!!)
        } else requestSessions(loginData.userDetail?.batchList?.get(0)?.additionalCourseId!!)

    }

    private fun subjectCall(subjectList: ArrayList<Datum>) {
        stateful.showContent()

          val manager = FlexboxLayoutManager(requireContext(), FlexDirection.COLUMN)
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

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.icon_error)
        stateful.setOfflineRetryOnClickListener {
            if (loginData.userDetail?.batchList?.get(0)?.additionalCourseId.isNullOrEmpty()) {
                requestSessions(loginData.userDetail?.batchList?.get(0)?.courseId!!)
            } else requestSessions(loginData.userDetail?.batchList?.get(0)?.additionalCourseId!!)
        }
    }

    private fun requestSessions(batchId: String) {
        stateful.showProgress()
        val headers = HashMap<String, String>()

        networkHelper.getCall(
            URLHelper.courseURL + batchId,
            "getCourse",
            headers,
            this
        )

    }

    override fun onSubjectClicked(Id: String, batchId: String, title: String) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        intent.putExtra("id", Id)
        intent.putExtra("batchId", batchId)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    override fun onTestClicked() {
        val intent = Intent(requireContext(), TestVideoActivity::class.java)
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
            FragmentLearnCommon().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM2, param1)
                }
            }
    }
}