package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.SubjectListAdapter
import kotlinx.android.synthetic.main.activity_chapter.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class ChapterActivity : AppCompatActivity() {

    private var chapterList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapter)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Learn"

        chapterList.apply {
            this.add("Physics World")
            this.add("Law of Motions")
            this.add("Conservation of Energy")
            this.add("Heat and Temperature")
            this.add("Wave Energy")
            this.add("Kinematics")
            this.add("Dynamics: Forces and Motion")
            this.add("Impulse and Momentum")
            this.add("Astronomy")
            this.add("Electricity and Electrical Energy")
            this.add("Nature and Behavior of Light")
        }

        subjectListCall()

    }

    private fun subjectListCall() {
        //adding a layoutmanager
        recyclerviewsubjectslist.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = SubjectListAdapter(this, chapterList)

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

}