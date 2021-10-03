package com.upmyranksapp.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.upmyranksapp.R
import com.upmyranksapp.adapter.SubjectListAdapter
import com.upmyranksapp.helper.MyProgressBar
import com.upmyranksapp.model.CourseResponse
import com.upmyranksapp.model.Datum
import com.upmyranksapp.model.TopicResponse
import com.upmyranksapp.model.chapter.ChapterResponse
import com.upmyranksapp.model.chapter.ChapterResponseData
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.ApiUtils
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.network.URLHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.layout_recyclerview.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ChapterActivity : AppCompatActivity(), OnNetworkResponse {

    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper
    lateinit var subjectId: String
    lateinit var batchId: String
    lateinit var chapterResponse: ChapterResponse
    lateinit var myProgressBar: MyProgressBar
    private var loginData = LoginData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
        myProgressBar = MyProgressBar(this)

        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)


        subjectId = intent.getStringExtra("id")!!
        batchId = intent.getStringExtra("batchId")!!

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = intent.getStringExtra("title")

        requestChapter(batchId)
    }

    private fun requestChapter(batchId: String) {
        stateful.showProgress()
        stateful.setProgressText("Chapters loading, Please wait..")
        networkHelper.getCall(
            URLHelper.courseURL1  + "?id=${subjectId}&batchId=$batchId",
            "getChapter",
            ApiUtils.getHeader(this),
            this
        )
    }

/*
    private fun requestChapter1(subjectList: Datum) {
        stateful.showProgress()
        stateful.setProgressText("Loading..")
        networkHelper.getArrayCall(
            URLHelper.publishedMaterialsByChapter + "?chapterId=${subjectList.id}&batchId=$batchId",
            "publishedMaterialsByChapter",
            ApiUtils.getHeader(this),
            this
        )
    }
*/

    private fun subjectListCall(subjectList: List<ChapterResponseData>) {
        if (subjectList.size > 0) {

            supportActionBar?.subtitle = "${subjectList.size} Chapters"
            //adding a layoutmanager
            recyclerView.layoutManager =
                LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)
            recyclerView.setPadding(16, 0, 16, 0)
            val adapter = SubjectListAdapter(applicationContext, subjectList, batchId)

            //now adding the adapter to recyclerview
            recyclerView.adapter = adapter


        } else {
            showErrorMsg("No chapters found, Please try again.")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_learn, menu)
            val item1 =
                menu.findItem(R.id.action_menu_notification).actionView.findViewById(R.id.layoutNotification) as RelativeLayout
            item1.setOnClickListener {
                startActivity(Intent(this, NotificationsActivity::class.java))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (responseCode == networkHelper.responseSuccess && tag == "getChapter") {
            stateful.showContent()
            chapterResponse = Gson().fromJson(response, ChapterResponse::class.java)
            chapterResponse.data?.let { subjectListCall(it) }
        } else {
            showErrorMsg(resources.getString(R.string.sfl_default_error))
        }
    }

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.ic_no_data)
        stateful.setOfflineRetryOnClickListener {
            requestChapter(subjectId)
        }
    }

}