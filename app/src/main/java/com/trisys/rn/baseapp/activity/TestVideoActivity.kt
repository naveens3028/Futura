package com.trisys.rn.baseapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.Learn.OnVideoClicked
import com.trisys.rn.baseapp.adapter.Learn.VideoLearnAdapter
import com.trisys.rn.baseapp.helper.exoplayer.ExoUtil
import com.trisys.rn.baseapp.model.VideoDataModel
import kotlinx.android.synthetic.main.activity_test_video.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*

class TestVideoActivity: AppCompatActivity() , OnVideoClicked {

    private val videoList = ArrayList<VideoDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_video)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Test Result"

        val url = "https://dyshz3enlsbtb.cloudfront.net/Entrance/Physics+11/8.+Gravitation/"
        videoList.apply {
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T1+Newtons+law+of+Gravitation.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T10+Relation+between+g+&+G.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T11+Variation+of+g+with+height.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T12+Variation+of+g+with+depth.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T13+Variation+of+g+with+latitude.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T14+Revision+of+force,+intensity+&+potetial+formulae.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T15+Gravitational+Intensity+and+potential+for+spherical+shell.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T16+Example+problem+on+Gravitational+Intensity+and+potential+for+spherical+shell.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T17+Gravitational+Intensity+&+Potential+for+Solid+sphere.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T18+Gravitational+Intensity+&+Potential+for+Ring.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T19+Escape+velocity.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T2+Example+problem1&2+for+calculation+of+force.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T20+Orbital+Velocity.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T21+Kepler's+Laws+of+planetary+motion.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T3+Example+problem+3+for+calculation+of+force.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T4+Example+4+for+calculation+of+force.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T6+Gravitational+intensity+due+to+point+mass.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T7+Gravitational+Potential+due+to+point+mass.mp4"))
            this.add(VideoDataModel(UUID.randomUUID().toString(), url+"T9+Example+problem+on+potential+energy.mp4"))
        }
        callRecycler()
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

    @SuppressLint("WrongConstant")
    private fun callRecycler(){
        testRecycler.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        val adapter = VideoLearnAdapter(this, videoList, this)
        testRecycler.adapter = adapter
    }

    override fun onVideoSelected(url: String) {
        ExoUtil.buildMediaItems(this,supportFragmentManager,url)
    }

}