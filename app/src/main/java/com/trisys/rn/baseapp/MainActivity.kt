package com.trisys.rn.baseapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
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
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import com.trisys.rn.baseapp.activity.NotificationsActivity
import com.trisys.rn.baseapp.adapter.HomeTabViewAdapter
import com.trisys.rn.baseapp.database.DatabaseHelper
import com.trisys.rn.baseapp.doubt.AskDoubtActivity
import com.trisys.rn.baseapp.fragment.LogOutBottomSheetFragment
import com.trisys.rn.baseapp.fragment.UpdateBottomSheetFragment
import com.trisys.rn.baseapp.helper.BottomNavigationBehavior
import com.trisys.rn.baseapp.model.OnEventData
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.profile.ProfileActivity
import com.trisys.rn.baseapp.qrCode.QRCodeActivity
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.ImageLoader
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_notification_icon.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.layout_toolbar_custom.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import org.greenrobot.eventbus.EventBus


class MainActivity : AppCompatActivity(), OnNetworkResponse, InstallStateUpdatedListener {
    private lateinit var appUpdateManager: AppUpdateManager
    lateinit var mRemoteConfig: FirebaseRemoteConfig
    var cacheExpiration: Long = 3600
    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private val REQUEST_CODE_IMMEDIATE_UPDATE = 17362
    }

    lateinit var homeTabViewAdapter: HomeTabViewAdapter
    lateinit var bottomNavigationBehavior: BottomNavigationBehavior
    lateinit var networkHelper: NetworkHelper
    lateinit var headerLayout: View
    lateinit var loginResponse: LoginData
    private val imageLoader = ImageLoader
    lateinit var databaseHelper: DatabaseHelper
    lateinit var myPreferences: MyPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Assign Appbar properties
       // setSupportActionBar(toolbar_top)
/*        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)*/
        myPreferences = MyPreferences(this)
        mRemoteConfig = FirebaseRemoteConfig.getInstance()

        loginResponse =
            Gson().fromJson(MyPreferences(this).getString(Define.LOGIN_DATA), LoginData::class.java)


        //in app update checker
        appUpdateManager = AppUpdateManagerFactory.create(this)
        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(this)
        // Returns an intent object that you use to check for an update.
        val appUpdateInfoTask = appUpdateManager.appUpdateInfo


        if(mRemoteConfig.getBoolean(Define.IN_APP_UPDATE_ENABLE)) {
            appUpdateInfoTask.addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                        AppUpdateType.IMMEDIATE
                    )
                ) {   //  check for the type of update flow you want
                    requestUpdate(it)
                }
            }
        }


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


        //check update via remoteconfig
        updateViews()

        setMenuItems()
    }

    private fun setMenuItems() {
        userNameTool.text = "Hello Tina,"

        val newList = ArrayList<String>()
        newList.apply {
            loginResponse.userDetail?.batchList?.forEach {
                this.add(it.batchName.toString())
            }
            Log.e("popData", newList.toString())
            val adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, newList)
            batchSpinner.adapter = adapter

            batchSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    Log.e("popThread", "1234")

                    EventBus.getDefault().post(OnEventData(i))
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
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
                    bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
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
        mRemoteConfig.fetch(cacheExpiration)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // task successful. Activate the fetched data
                    mRemoteConfig.activate()
                    //update views?
                    updateViews()
                }
            }
        val pageChangeCallback: ViewPager2.OnPageChangeCallback =
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (position == 0) {
                        navigationView.selectedItemId = R.id.navigation_learn
                    } else if (position == 1) {
                        navigationView.selectedItemId = R.id.navigation_live
                    } /*else if (position == 2) {
                        navigationView.selectedItemId = R.id.navigation_home
                    }*/ else if (position == 2) {
                        navigationView.selectedItemId = R.id.navigation_practice
                    } else if (position == 3) {
                        navigationView.selectedItemId = R.id.navigation_test
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {
                    //appBarLayout.setExpanded(true)
                    bottomNavigationBehavior.showBottomNavigationView(navigationView)
                }
            }
        viewPager.registerOnPageChangeCallback(pageChangeCallback)

        navigationView.setOnNavigationItemSelectedListener(
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.navigation_learn -> {
                        viewPager.currentItem = 0
                     //   supportActionBar!!.title = ""
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_live -> {
                        viewPager.currentItem = 1
                     //   supportActionBar!!.title = ""
                        return@OnNavigationItemSelectedListener true
                    }
                    /*  R.id.navigation_home -> {
                          viewPager.currentItem = 2
                          supportActionBar!!.title = ""
                          return@OnNavigationItemSelectedListener true
                      }*/
                    R.id.navigation_practice -> {
                        viewPager.currentItem = 2
                       // supportActionBar!!.title = ""
                        return@OnNavigationItemSelectedListener true
                    }
                    R.id.navigation_test -> {
                        viewPager.currentItem = 3
                       // supportActionBar!!.title = ""
                        return@OnNavigationItemSelectedListener true
                    }
                }
                false
            })

        viewPager.currentItem = myPreferences.getInt(Define.HOME_SCREEN_LAST_KNOWN_TAB_POSITION, 0)
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {

    }

/*
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        val spinner = menu.findItem(R.id.action_menu_spinner).actionView as Spinner
        val item1 =
            menu.findItem(R.id.action_menu_notification).actionView.findViewById(R.id.layoutNotification) as RelativeLayout
        item1.setOnClickListener {
            startActivity(Intent(this, NotificationsActivity::class.java))
        }
        val newList = ArrayList<String>()
        newList.apply {
            loginResponse.userDetail?.batchList?.forEach {
                this.add(it.batchName.toString())
            }
            Log.e("popData", newList.toString())
            val adapter = ArrayAdapter(applicationContext, R.layout.spinner_item, newList)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                    Log.e("popThread", "1234")

                    EventBus.getDefault().post(OnEventData(i))
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
        }
        return true
    }
*/

/*
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
*/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            openQRCodeScreen()
        }
    }

    fun openQRCodeScreen() {
        val intent = Intent(this, QRCodeActivity::class.java)
        startActivity(intent)
    }

    override fun onPause() {
        super.onPause()
        myPreferences.setInt(Define.HOME_SCREEN_LAST_KNOWN_TAB_POSITION, viewPager.currentItem)
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
                myPreferences.setInt(Define.HOME_SCREEN_LAST_KNOWN_TAB_POSITION, 0)
                this.moveTaskToBack(true)
                System.exit(1)
                this.finish()
            }
        }
    }

    //in app update request method
    private fun requestUpdate(appUpdateInfo: AppUpdateInfo?) {
        try {
            if (appUpdateInfo != null) {
                appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE, //  HERE specify the type of update flow you want
                    this,   //  the instance of an activity
                    REQUEST_CODE_IMMEDIATE_UPDATE
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onResume() {
        super.onResume()
        try {
            appUpdateManager.appUpdateInfo.addOnSuccessListener {
                if (it.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                    appUpdateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        this,
                        REQUEST_CODE_IMMEDIATE_UPDATE
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMMEDIATE_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("MY_APP", "Update flow failed! Result code: $resultCode")
                // If the update is cancelled or fails,
                // you can request to start the update again.
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(this)
    }
    override fun onStateUpdate(state: InstallState) {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            notifyUser()
        }
    }
    private fun notifyUser() {
        Snackbar
            .make(toolbar, R.string.restart_to_update, Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.action_restart) {
                appUpdateManager.completeUpdate()
                appUpdateManager.unregisterListener(this)
            }
            .show()
    }
    private fun updateViews() {
        if (mRemoteConfig.getLong(Define.APP_VERSION_CODE) != Utils.getAppVersionCode(this)) {
            appUpdateDialog()
        }
    }
    private fun appUpdateDialog(){
        val bottomSheetFragment = UpdateBottomSheetFragment()
        bottomSheetFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        if(mRemoteConfig.getBoolean(Define.APP_FORCE_UPDATE)) {
            bottomSheetFragment.isCancelable = false
        }
    }
}