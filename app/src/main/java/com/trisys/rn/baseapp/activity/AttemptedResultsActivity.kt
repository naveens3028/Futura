package com.trisys.rn.baseapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.RelativeLayout
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.androidnetworking.common.Priority
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.adapter.test.AllResultsAdapter
import com.trisys.rn.baseapp.helper.MyProgressBar
import com.trisys.rn.baseapp.model.TestResultsData
import com.trisys.rn.baseapp.model.onBoarding.LoginData
import com.trisys.rn.baseapp.network.ApiUtils
import com.trisys.rn.baseapp.network.NetworkHelper
import com.trisys.rn.baseapp.network.OnNetworkResponse
import com.trisys.rn.baseapp.network.URLHelper
import com.trisys.rn.baseapp.network.URLHelper.answeredTestPaper
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import kotlinx.android.synthetic.main.activity_all_results.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import org.json.JSONException
import org.json.JSONObject

class AttemptedResultsActivity : AppCompatActivity(), OnNetworkResponse {

    private var loginData = LoginData()
    lateinit var networkHelper: NetworkHelper
    lateinit var myPreferences: MyPreferences
    lateinit var myProgressBar: MyProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_results)

        myPreferences = MyPreferences(this)
        networkHelper = NetworkHelper(this)
        myProgressBar = MyProgressBar(this)


        loginData =
            Gson().fromJson(myPreferences.getString(Define.LOGIN_DATA), LoginData::class.java)

        //Assign Appbar properties
        setSupportActionBar(toolbar)
        val actionBar: ActionBar? = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        actionBar?.title = "Test Result"

        requestSessions()
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


    private fun requestSessions() {

        myProgressBar.show()

        val params = HashMap<String, String>()
        params["studentId"] = loginData.userDetail?.usersId.toString()

        networkHelper.call(
            networkHelper.GET,
            networkHelper.RESTYPE_ARRAY,
            URLHelper.testResultUrl,
            params,
            Priority.HIGH,
            "getResults",
            this
        )

        val jsonObject = JSONObject()
        try {
            jsonObject.put("attempt", "1")
            jsonObject.put("studentId", "5085517d-90af-49ae-b95b-68c7ec234363")
            jsonObject.put("testPaperId", "8571b238-3628-4935-b233-b2d8cba32865")
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        networkHelper.postCall(
            answeredTestPaper,
            jsonObject,
            "answeredTestPaper",
            ApiUtils.getHeader(this),
            this
        )

    }

    private fun recyclerCall(resultList: ArrayList<TestResultsData>) {
        val adapter = AllResultsAdapter(this, resultList)
        //now adding the adapter to recyclerview
        allResultsRecycler.adapter = adapter
    }

    override fun onNetworkResponse(responseCode: Int, response: String, tag: String) {
        if (tag == "getResults") {
            val arrayTutorialType = object : TypeToken<ArrayList<TestResultsData>>() {}.type
            val newList: ArrayList<TestResultsData> = Gson().fromJson(response, arrayTutorialType)
            myProgressBar.dismiss()
            recyclerCall(newList)
        }
    }

}