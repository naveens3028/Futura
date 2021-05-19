package com.trisys.rn.baseapp.learn

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.trisys.rn.baseapp.Model.SubTopicItem
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.SubTopicsAdapter
import com.trisys.rn.baseapp.adapter.SubTopicsTitleAdapter
import kotlinx.android.synthetic.main.activity_learn.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class LearnActivity : AppCompatActivity() {
    private var subTopicList = ArrayList<SubTopicItem>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Mathematical Physics"


        subTopicList.add(SubTopicItem("Trigonometry"))
        subTopicList.add(SubTopicItem("Basics of Trigonometry & Trigonometry"))
        subTopicList.add(SubTopicItem("T3 Star"))
        subTopicList.add(SubTopicItem("Biology"))

        val titleRecyclerView = titleRecycler
        val titleAdapter = SubTopicsTitleAdapter(this, subTopicList)
        titleRecyclerView.adapter = titleAdapter

        val subTopicsRecyclerView = supTopicRecycler
        val subTopicListAdapter = SubTopicsAdapter(this, subTopicList)
        subTopicsRecyclerView.adapter = subTopicListAdapter

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
}
