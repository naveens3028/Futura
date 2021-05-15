package com.trisys.rn.baseapp

import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.androidnetworking.common.Priority
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.trisys.rn.baseapp.adapter.HomeTabViewAdapter
import com.trisys.rn.baseapp.helper.BottomNavigationBehavior
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class MainActivity : AppCompatActivity(), OnNetworkResponse {
    lateinit var homeTabViewAdapter : HomeTabViewAdapter
    lateinit var bottomNavigationBehavior: BottomNavigationBehavior
    lateinit var networkHelper:NetworkHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar!!.setHomeActionContentDescription("Navigation Menu")

        val actionbar: ActionBar? = supportActionBar
        actionbar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(Utils.getFontDrawableIcon(applicationContext,"menu",R.color.white,30f))
        }
        val drawer = findViewById(R.id.drawer_layout) as DrawerLayout
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        networkHelper = NetworkHelper(this)

        val params = HashMap<String,String>()
        params.put("","")
        networkHelper.call(networkHelper.GET,"",params,Priority.HIGH,"login",this)

    }

    override fun onStart() {
        super.onStart()


        bottomNavigationBehavior = BottomNavigationBehavior()
        val layoutParams = navigationView.getLayoutParams() as CoordinatorLayout.LayoutParams
        layoutParams.behavior = bottomNavigationBehavior

        homeTabViewAdapter = HomeTabViewAdapter(this)
        viewPager.adapter = homeTabViewAdapter
        viewPager.offscreenPageLimit = 3


        val pageChangeCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0){
                    navigationView.setSelectedItemId(R.id.navigation_home);
                }else if(position == 1){
                    navigationView.setSelectedItemId(R.id.navigation_learn);
                }else if(position == 2){
                    navigationView.setSelectedItemId(R.id.navigation_live);
                }else if(position == 3){
                    navigationView.setSelectedItemId(R.id.navigation_test);
                }else if(position == 4){
                    navigationView.setSelectedItemId(R.id.navigation_doubts);
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
}