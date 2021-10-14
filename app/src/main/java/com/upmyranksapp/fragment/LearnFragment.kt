package com.upmyranksapp.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.activity.ChapterActivity
import com.upmyranksapp.adapter.SubjectClickListener
import com.upmyranksapp.adapter.SubjectsAdapter
import com.upmyranksapp.adapter.VideoPlayedAdapter
import com.upmyranksapp.database.AppDatabase
import com.upmyranksapp.database.model.VideoPlayedItem
import com.upmyranksapp.fragment.practiceTest.CourseListener
import com.upmyranksapp.helper.exoplayer.ExoUtil
import com.upmyranksapp.model.CourseResponse
import com.upmyranksapp.model.Datum
import com.upmyranksapp.model.OnEventData
import com.upmyranksapp.model.VideoMaterial
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.fragment_learn.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import vimeoextractor.OnVimeoExtractionListener
import vimeoextractor.VimeoExtractor
import vimeoextractor.VimeoVideo
import java.util.*

class LearnFragment : Fragment(), CourseListener, VideoPlayedAdapter.ActionCallback,
    OnNetworkResponse, SubjectClickListener {

    private lateinit var courseRecycler: RecyclerView
    private var loginData = LoginData()
    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper
    var batchIds: String? = null
    var courseId: String? = null
    lateinit var db: AppDatabase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myPreferences = MyPreferences(requireContext())
        networkHelper = NetworkHelper(requireContext())
        db = AppDatabase.getInstance(requireContext())!!
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

        Log.e("popAns","1")

        batchIds = if (loginData.userDetail?.batchList!![0].additionalCourseId.isNullOrEmpty()) {
            loginData.userDetail?.batchList!![0].id
        } else {
            loginData.userDetail?.batchList!![0].additionalCourseId
        }

        if (!courseId.isNullOrEmpty()){
            requestSessions(courseId!!)
        } else if (loginData.userDetail?.batchList?.get(0)?.additionalCourseId.isNullOrEmpty()) {
            requestSessions(loginData.userDetail?.batchList?.get(0)?.courseId!!)
        } else requestSessions(loginData.userDetail?.batchList?.get(0)?.additionalCourseId!!)

        selectedCourseTxt.text = "Course Name: ${loginData.userDetail?.batchList?.get(0)?.course?.courseName!!}"
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onResume() {
        super.onResume()
        if (db.videoDao.getAll().isNullOrEmpty()) {
            previosVideo.visibility = View.GONE
        } else {
            previosVideo.visibility = View.VISIBLE
        }
        courseCall()
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


    @SuppressLint("WrongConstant")
    private fun courseCall() {
        val videoRecyclerView = view?.findViewById(R.id.playedRecycler) as RecyclerView
        val videoAdapter =
            VideoPlayedAdapter(requireActivity(), "0", db.videoDao.getAll(), null, this)
        videoRecyclerView.adapter = videoAdapter
    }

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.icon_error)
        stateful.setOfflineRetryOnClickListener {
        }
    }

    override fun onCourseClicked(batchId: String, id: String, position: Int) {
        courseRecycler.layoutManager?.scrollToPosition(position)
        batchIds = id
    }

    override fun onVideoClickListener(videoPlayedItem: VideoPlayedItem) {
        if (videoPlayedItem.videoUrl!!.contains("vimeo", true)) {
            playVideo(videoPlayedItem.videoTitle!!, videoPlayedItem.videoUrl!!)
        }else{
            ExoUtil.buildMediaItems(
                requireActivity(),
                childFragmentManager,
                videoPlayedItem.videoTitle!!,
                videoPlayedItem.videoUrl!!,
                false
            )
        }
    }

    override fun onVideoClickListener1(videoPlayedItem: VideoMaterial) {
    }

    private fun playVideo(title: String, id: String) {
        val videoId = id.replace("https://vimeo.com/", "")
        VimeoExtractor.getInstance()
            .fetchVideoWithIdentifier(videoId, null, object : OnVimeoExtractionListener {
                override fun onSuccess(video: VimeoVideo) {
                    val hdStream = video.streams["720p"]
                    println("VIMEO VIDEO STREAM$hdStream")
                    hdStream?.let {
                        requireActivity().runOnUiThread {
                            //code that runs in main
                            navigateVideoPlayer(title, it)
                        }
                    }
                }

                override fun onFailure(throwable: Throwable) {
                    Log.d("failure", throwable.message!!)
                }
            })
    }

    fun navigateVideoPlayer(title: String, url: String) {
        ExoUtil.buildMediaItems(
            requireActivity(),
            childFragmentManager, title,
            url, false
        )
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "getCourse") {
            val courseResponse = Gson().fromJson(response, CourseResponse::class.java)
            subjectCall(courseResponse.data!!)
        } else {
            showErrorMsg(requireActivity().getString(R.string.sfl_default_error))
        }
    }

    override fun onSubjectClicked(Id: String, batchId: String, title: String) {
        val intent = Intent(requireContext(), ChapterActivity::class.java)
        intent.putExtra("id", Id)
        intent.putExtra("batchId", batchIds)
        intent.putExtra("title", title)
        startActivity(intent)
    }

    private fun subjectCall(subjectList: ArrayList<Datum>) {
        stateful.showContent()
        if (subjectList.size > 0) {
            val adapter = SubjectsAdapter(requireContext(), subjectList, batchIds.toString(), this)
            //now adding the adapter to recyclerview
            subjectsRecycler.adapter = adapter
        } else {
            showErrorMsg("No subject found.")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: OnEventData?) {
        Log.e("popThread","123")
        val data = loginData.userDetail?.batchList?.get(event?.batchPosition!!)
        requestSessions(loginData.userDetail?.batchList?.get(event?.batchPosition!!)?.courseId!!)
        selectedCourseTxt.text = "Course Name: ${loginData.userDetail?.batchList?.get(event?.batchPosition!!)?.course?.courseName!!}"
        batchIds = if (!data?.additionalCourseId.isNullOrEmpty()){
            data?.additionalCourseId
        }else{
            data?.id
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }
}