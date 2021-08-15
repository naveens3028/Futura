package com.trisys.rn.baseapp.learn

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.SubTopicsAdapter
import com.trisys.rn.baseapp.adapter.SubTopicsTitleAdapter
import com.trisys.rn.baseapp.model.TopicResponse
import com.trisys.rn.baseapp.model.VideoMaterial
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper.publishedMaterialsByChapter
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class LearnActivity : AppCompatActivity(), OnNetworkResponse, TopicClickListener {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    var topicResponse = TopicResponse()
    lateinit var subTopicListAdapter: SubTopicsAdapter
    var chapterId = ""
    var batchId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = intent.getStringExtra("title")

        chapterId = intent.getStringExtra("id")!!
        batchId = intent.getStringExtra("batchID")!!

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)
        requestChapter()

    }

    private fun requestChapter() {
        stateful.showProgress()
        stateful.setProgressText("Loading..")
        networkHelper.getArrayCall(
            publishedMaterialsByChapter + "?chapterId=$chapterId&batchId=$batchId",
            "publishedMaterialsByChapter",
            ApiUtils.getHeader(this),
            this
        )
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
        stateful.showContent()
        if (responseCode == networkHelper.responseSuccess && tag == "publishedMaterialsByChapter") {
            val topicResponse = Gson().fromJson(response, TopicResponse::class.java)
            if (topicResponse.isNotEmpty()) {
                val titleAdapter = SubTopicsTitleAdapter(this, topicResponse, this)
                titleRecycler.adapter = titleAdapter
                if (topicResponse[0].materialList != null && topicResponse[0].materialList?.size!! > 0) {
                    supTopicRecycler.visibility = View.VISIBLE
                    subTopicListAdapter =
                        SubTopicsAdapter(this, topicResponse[0].materialList!!)
                    supTopicRecycler.adapter = subTopicListAdapter
                } else {
                    supTopicRecycler.visibility = View.GONE
                    showErrorMsg("Currently no topics available.")
                }
            } else {
                showErrorMsg(resources.getString(R.string.sfl_default_error))
            }
        }
    }

    override fun onTopicSelected(subTopicItems: List<VideoMaterial>) {
        subTopicListAdapter = SubTopicsAdapter(this, subTopicItems)
        supTopicRecycler.adapter = subTopicListAdapter
    }

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.icon_error)
        stateful.setOfflineRetryOnClickListener {
            requestChapter()
        }
    }
}
