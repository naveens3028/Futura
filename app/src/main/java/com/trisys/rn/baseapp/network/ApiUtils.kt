package com.trisys.rn.baseapp.network

import android.content.Context
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
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
        headers["Content-Type"] = "application/json; charset=utf-8"
        headers["access_token"] = myPreferences.getString(Define.ACCESS_TOKEN)
        return headers
    }
}