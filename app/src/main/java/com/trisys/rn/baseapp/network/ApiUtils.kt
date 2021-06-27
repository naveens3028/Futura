package com.trisys.rn.baseapp.network

import android.content.Context
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import okhttp3.internal.Util
import java.util.*

object ApiUtils {

    fun getHeader(): MutableMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        return headers
    }

    fun getAuthorizationHeader(context: Context?): MutableMap<String, String> {
        val myPreferences = MyPreferences(context)
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json;charset=utf-8"
        headers["Content-Length"] = "252"
        headers["Host"] = "api.upmyranks.com"

//        headers["Content-Type"] = "application/json"
        headers["access_token"] = "72640d71-abab-4db5-bfa9-d9f4ca0ebcd0"
        Utils.log("s2s","access_token ${myPreferences.getString(Define.ACCESS_TOKEN).toString()}")
        return headers
    }
}