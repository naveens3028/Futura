package com.upmyranksapp.learn

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
import com.upmyranksapp.R
import com.upmyranksapp.activity.NotificationsActivity
import com.upmyranksapp.adapter.Learn.LearnTopicHeaderAdapter
import com.upmyranksapp.adapter.Learn.TopicVideoAdapter
import com.upmyranksapp.adapter.SubTopicsAdapter
import com.upmyranksapp.model.chapter.TopicMaterialResponse
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.row_topic_items.*


class LearnActivity : AppCompatActivity() {

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

        if (topicResponse!!.isNotEmpty()) {
            val titleAdapter = TopicVideoAdapter(this, topicResponse)
            tabsRecycler.adapter = titleAdapter
/*
            if (topicResponse!![0].materialList != null && topicResponse!![0].materialList?.size!! > 0) {
                supTopicRecycler.visibility = View.VISIBLE
                subTopicListAdapter =
                    SubTopicsAdapter(this, topicResponse[0].materialList!!)
                supTopicRecycler.adapter = subTopicListAdapter
            } else {
                supTopicRecycler.visibility = View.GONE
                showErrorMsg("Currently no topics available.")
            }
*/
        } else {
            tabsRecycler.visibility = View.GONE
            showErrorMsg("Currently no topics available.")
        }
/*
        if (!topicResponse.isNullOrEmpty()) {
        //    materialsPagerAdapter = MaterialsPagerAdapter(this, topicResponse!!)
           */
/* view_pager_material.adapter = materialsPagerAdapter
            view_pager_material.offscreenPageLimit = 3


            view_pager_material.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
            })

            TabLayoutMediator(tabs_material, view_pager_material) { tab, position ->
                tab.text = topicResponse!![position].topic?.courseName
            }.attach()

            val pageChangeCallback: ViewPager2.OnPageChangeCallback =
                object : ViewPager2.OnPageChangeCallback() {

                    override fun onPageScrollStateChanged(state: Int) {
                        appBarLayout.setExpanded(true)
                    }
                }
            view_pager_material.registerOnPageChangeCallback(pageChangeCallback)*//*

        }
*/

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


    /* override fun onTopicSelected(subTopicItems: List<VideoMaterial>) {
         subTopicListAdapter = SubTopicsAdapter(this, subTopicItems)
         supTopicRecycler.adapter = subTopicListAdapter
     }*/

    fun showErrorMsg(errorMsg: String) {
        stateful.showOffline()
        stateful.setOfflineText(errorMsg)
        stateful.setOfflineImageResource(R.drawable.icon_error)
    }
}
