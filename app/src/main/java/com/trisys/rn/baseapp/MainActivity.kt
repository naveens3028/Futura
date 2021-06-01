package com.trisys.rn.baseapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.androidnetworking.common.Priority
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.HomeTabViewAdapter
import com.trisys.rn.baseapp.helper.BottomNavigationBehavior
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_notification_icon.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*


class MainActivity : AppCompatActivity(), OnNetworkResponse {
    lateinit var homeTabViewAdapter: HomeTabViewAdapter
    lateinit var bottomNavigationBehavior: BottomNavigationBehavior
    lateinit var networkHelper: NetworkHelper
    lateinit var headerLayout: View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.title = "Hi, John"

        //Assign Drawer properties
        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //Get header view
        headerLayout = nav_view.getHeaderView(0)

        listeners()

        networkHelper = NetworkHelper(this)

        val params = HashMap<String, String>()
        params.put("", "")
        networkHelper.call(networkHelper.GET, "", params, Priority.HIGH, "login", this)

        bottomNavigationBehavior = BottomNavigationBehavior()
        val layoutParams = navigationView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = bottomNavigationBehavior

        homeTabViewAdapter = HomeTabViewAdapter(this)
        viewPager.adapter = homeTabViewAdapter
        viewPager.offscreenPageLimit = 3
    }

    private fun listeners() {

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_home -> true
            }
            true
        }

        headerLayout.close.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }
    }

    override fun onStart() {
        super.onStart()

        val pageChangeCallback: ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        navigationView.selectedItemId = R.id.navigation_home
                    } else if (position == 1) {
                        navigationView.selectedItemId = R.id.navigation_learn
                    } else if (position == 2) {
                        navigationView.selectedItemId = R.id.navigation_live
                    } else if (position == 3) {
                        navigationView.selectedItemId = R.id.navigation_test
                    } else if (position == 4) {
                        navigationView.selectedItemId = R.id.navigation_doubts
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    appBarLayout.setExpanded(true)
                    bottomNavigationBehavior.showBottomNavigationView(navigationView)
                }
            }
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        navigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_home -> {
                        viewPager.currentItem = 0
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_learn -> {
                        viewPager.currentItem = 1
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_live -> {
                        viewPager.currentItem = 2
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_test -> {
                        viewPager.currentItem = 3
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_doubts -> {
                        viewPager.currentItem = 4
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        try {
            menuInflater.inflate(R.menu.menu_home, menu)
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
        super.onOptionsItemSelected(item)
        return when (item.itemId) {
            R.id.action_menu_notification -> {
                return true
            }
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        //Back event handled when drawer open
        when {
            drawer.isDrawerOpen(GravityCompat.START) -> {
                drawer.closeDrawer(GravityCompat.START)
            }
            viewPager.currentItem != 0 -> {
                viewPager.setCurrentItem(0, true)
            }
            else -> {
                this.finish()
            }
        }
    }
}