package com.trisys.rn.baseapp.network

import android.content.Context
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import java.util.*

object ApiUtils {

    fun getHeader(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        headers["Content-Type"] = "application/json"
        return headers
    }

    fun getAuthorizationHeader(context: Context?,contentLength:Int): HashMap<String, String> {
        val myPreferences = MyPreferences(context)
        val headers = HashMap<String, String>()
        headers["access_token"] = myPreferences.getString(Define.ACCESS_TOKEN).toString()
        headers["Content-Type"] = "application/json; charset=UTF-8"
        headers["Content-Length"] = contentLength.toString()
        headers["Host"] = "api.upmyranks.com"
        Utils.log(
            "NetworkHelper",
            "access_token ${myPreferences.getString(Define.ACCESS_TOKEN).toString()}"
        )
        return headers
    }

    fun getHeader(context: Context?): HashMap<String, String> {
        val myPreferences = MyPreferences(context)
        val headers = HashMap<String, String>()
        headers["access_token"] = myPreferences.getString(Define.ACCESS_TOKEN).toString()
        Utils.log(
            "NetworkHelper",
            "access_token ${myPreferences.getString(Define.ACCESS_TOKEN).toString()}"
        )
        return headers
    }

}