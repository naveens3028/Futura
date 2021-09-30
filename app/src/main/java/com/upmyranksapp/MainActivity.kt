package com.upmyranksapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.DialogFragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.upmyranksapp.activity.NotificationsActivity
import com.upmyranksapp.adapter.HomeTabViewAdapter
import com.upmyranksapp.database.DatabaseHelper
import com.upmyranksapp.doubt.AskDoubtActivity
import com.upmyranksapp.fragment.LogOutBottomSheetFragment
import com.upmyranksapp.helper.BottomNavigationBehavior
import com.upmyranksapp.model.onBoarding.LoginData
import com.upmyranksapp.network.NetworkHelper
import com.upmyranksapp.network.OnNetworkResponse
import com.upmyranksapp.profile.ProfileActivity
import com.upmyranksapp.qrCode.QRCodeActivity
import com.upmyranksapp.utils.Define
import com.upmyranksapp.utils.ImageLoader
import com.upmyranksapp.utils.MyPreferences
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
    lateinit var loginResponse: LoginData
    private val imageLoader = ImageLoader
    lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        loginResponse =
            Gson().fromJson(MyPreferences(this).getString(Define.LOGIN_DATA), LoginData::class.java)

        actionBar?.title = "Hi, ${loginResponse.userDetail?.firstName}"


        //Assign Drawer properties
        val drawer = findViewById<DrawerLayout>(R.id.drawer)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //Get header view
        headerLayout = nav_view.getHeaderView(0)
        setNavigationValue(loginResponse)

        listeners()

        networkHelper = NetworkHelper(this)

        val params = HashMap<String, String>()
        params.put("", "")

        bottomNavigationBehavior = BottomNavigationBehavior()
        val layoutParams = navigationView.layoutParams as CoordinatorLayout.LayoutParams
        layoutParams.behavior = bottomNavigationBehavior

        homeTabViewAdapter = HomeTabViewAdapter(this)
        viewPager.adapter = homeTabViewAdapter
        viewPager.offscreenPageLimit = 3
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 4) askDoubt.visibility = View.GONE
                else askDoubt.visibility = View.GONE
            }
        })

        askDoubt.setOnClickListener {
            val intent = Intent(this, AskDoubtActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setNavigationValue(response: LoginData) {
        var userName = ""
        if (!response.userDetail?.firstName.isNullOrEmpty()) userName =
            response.userDetail?.userName.toString()
        if (userName.isNotEmpty()) {
            headerLayout.name.text = userName
        }
    }


    private fun listeners() {

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.logOut -> {
                    drawer.closeDrawer(GravityCompat.START)
                    val bottomSheetFragment = LogOutBottomSheetFragment()
                    bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL,R.style.DialogStyle)
                    bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
                }
                R.id.qrScanner -> {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        val intent = Intent(this, QRCodeActivity::class.java)
                        startActivity(intent)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.CAMERA),
                            100
                        )
                    }
                }
            }
            true
        }

        headerLayout.close.setOnClickListener {
            drawer.closeDrawer(GravityCompat.START)
        }

        headerLayout.profileImage.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
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
                        supportActionBar!!.title = "Hi, ${loginResponse.userDetail?.firstName}"
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_learn -> {
                        viewPager.currentItem = 1
                        supportActionBar!!.title = "Learn"
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_live -> {
                        viewPager.currentItem = 2
                        supportActionBar!!.title = "Live"
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_test -> {
                        viewPager.currentItem = 3
                        supportActionBar!!.title = "Test"
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_doubts -> {
                        viewPager.currentItem = 4
                        supportActionBar!!.title = "Doubts"
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
            R.id.action_qr_code -> {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED
                ) {
                    openQRCodeScreen()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.CAMERA),
                        100
                    )
                }
                return true
            }
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,permissions: Array<out String>,grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions!!, grantResults!!)
        if(requestCode == 100){
            openQRCodeScreen()
        }
    }

    fun openQRCodeScreen(){
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
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
                finish()
            }
        }
    }
}