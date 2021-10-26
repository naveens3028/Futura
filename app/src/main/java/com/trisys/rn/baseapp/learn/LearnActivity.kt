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
import com.google.gson.reflect.TypeToken
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.Learn.TopicVideoAdapter
import com.trisys.rn.baseapp.adapter.Learn.VideoClickListener
import com.trisys.rn.baseapp.adapter.SubTopicsAdapter
import com.trisys.rn.baseapp.model.VideoMaterial
import com.trisys.rn.baseapp.model.chapter.TopicMaterialResponse
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class LearnActivity : AppCompatActivity(), VideoClickListener {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    var topicResponse: List<TopicMaterialResponse>? = null
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

        val datas: List<TopicMaterialResponse> =
            Gson().fromJson(
                intent.getStringExtra("materials"),
                object : TypeToken<List<TopicMaterialResponse?>?>() {}.type
            )

        topicResponse = datas

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        val topicData = ArrayList<TopicMaterialResponse>()
        topicResponse!!.sortedBy {
            it.topic?.createdAt
        }.map {
            topicData.add(it)
        }

        if (topicResponse!!.isNotEmpty()) {
            val titleAdapter = TopicVideoAdapter(this, topicData , this)
            tabsRecycler.adapter = titleAdapter
        } else {
            tabsRecycler.visibility = View.GONE
            showErrorMsg("Currently no topics available.")
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

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.icon_error)
    }

    override fun onVideoSelected(videoMaterial: List<VideoMaterial>, position: Int) {
        myPreferences = MyPreferences(this)
        myPreferences.setString(Define.VIDEO_DATA, Gson().toJson(videoMaterial))
        myPreferences.setInt(Define.VIDEO_POS, position)
        val intent = Intent(this, LearnVideoActivity::class.java)
        startActivity(intent)
    }
}
