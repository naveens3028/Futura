import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.StudyAdapter
import com.trisys.rn.baseapp.fragment.CompletedLiveFragment
import com.trisys.rn.baseapp.fragment.UpcomingLiveFragment
import com.trisys.rn.baseapp.helper.MyProgressBar
import com.trisys.rn.baseapp.model.LiveResponse
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils.getAuthorizationHeader
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper.getSessions
import com.trisys.rn.baseapp.network.UrlConstants.kLIVE
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_live.*
import kotlinx.android.synthetic.main.fragment_upcoming_live.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LiveFragment : Fragment(), OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    lateinit var myProgressBar: MyProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
        myProgressBar = MyProgressBar(requireActivity())
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
                            .replace(
                                R.id.liveFrameLayout,
                                CompletedLiveFragment.newInstance("", "")
                            )
                            .commit()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            LiveFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun requestSessions() {

        val jsonObject = JSONObject()
        try {
            jsonObject.put("branchIds", JSONArray(loginData.userDetail?.branchIds))
            jsonObject.put("coachingCentreId", loginData.userDetail?.coachingCenterId.toString())
            jsonObject.put("batchIds", JSONArray(loginData.userDetail?.batchIds))
            jsonObject.put("sessionTense", kLIVE)
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        networkHelper.postCall(
            getSessions,
            jsonObject,
            "liveSessions",
            getAuthorizationHeader(requireContext(), jsonObject.toString().length),
            this
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        myProgressBar.dismiss()
        if (responseCode == networkHelper.responseSuccess && tag == "liveSessions") {
            val liveItemResponse = Gson().fromJson(response, LiveResponse::class.java)
            if (liveItemResponse.data.isNotEmpty()) {
                val studyAdapter = StudyAdapter(requireContext(), liveItemResponse.data)
                studyRecycler.adapter = studyAdapter
                errorLive.visibility = View.GONE
                recyclerScroll.visibility = View.VISIBLE
                upcomingSession.visibility = View.VISIBLE
                StudyLabel.visibility = View.VISIBLE
                noUpcomingSession.visibility = View.GONE
            }else{
                recyclerScroll.visibility = View.GONE
                errorLive.visibility = View.VISIBLE
                StudyLabel.visibility = View.VISIBLE
                retryLive.setOnClickListener {
                    myProgressBar.show()
                    requestSessions()
                }
            }
        }else{
            recyclerScroll.visibility = View.GONE
            errorLive.visibility = View.VISIBLE
            StudyLabel.visibility = View.VISIBLE
            retryLive.setOnClickListener {
                myProgressBar.show()
                requestSessions()
            }
        }
    }
}