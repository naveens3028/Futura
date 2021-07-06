package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.SubjectListAdapter
import com.trisys.rn.baseapp.model.CourseResponse
import com.trisys.rn.baseapp.model.Datum
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ChapterActivity : AppCompatActivity(), OnNetworkResponse {

    lateinit var myPreferences: MyPreferences
    lateinit var networkHelper: NetworkHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)

        val subjectId: String = intent.getStringExtra("id")!!

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Learn"

        requestChapter(subjectId)
    }

    private fun requestChapter(batchId: String) {
        networkHelper.getCall(
            URLHelper.courseURL + batchId,
            "getChapter",
            ApiUtils.getHeader(this),
            this
        )
    }

    private fun subjectListCall(subjectList: ArrayList<Datum>) {
        //adding a layoutmanager
        recyclerviewsubjectslist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = SubjectListAdapter(this, subjectList)

        //now adding the adapter to recyclerview
        recyclerviewsubjectslist.adapter = adapter
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
        val chapterResponse = Gson().fromJson(response, CourseResponse::class.java)
        chapterResponse.data?.let { subjectListCall(it) }
    }

}