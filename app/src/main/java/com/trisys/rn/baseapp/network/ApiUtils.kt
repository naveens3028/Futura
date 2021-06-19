package com.trisys.rn.baseapp.network

import android.content.Context
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import java.util.*

object ApiUtils {

    fun getHeader(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json; charset=utf-8"
        return headers
    }

    fun getAuthorizationHeader(context: Context?): HashMap<String, String> {
        val myPreferences = MyPreferences(context)
        val headers = HashMap<String, String>()
//        headers["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:89.0) Gecko/20100101 Firefox/89.0"
//        headers["Accept"] = "application/json"
//        headers["Accept-Language"] = "en-US,en;q=0.5"
        headers["Content-Type"] = "application/json; charset=utf-8"
        headers["access_token"] = myPreferences.getString(Define.ACCESS_TOKEN)
//        headers["Origin"] = "http://65.2.90.171"
        headers["Connection"] = "keep-alive"
//        headers["Referer"] = "http://65.2.90.171/material/studentLiveSession"
        return headers
    }
}