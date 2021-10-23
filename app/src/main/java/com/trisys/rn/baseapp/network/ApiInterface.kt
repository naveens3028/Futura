package com.trisys.rn.baseapp.network

import com.trisys.rn.baseapp.model.onBoarding.CompletedSession
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiInterface {

    @POST("session/getCompletedSessionsSubject")
    fun getData(@Body jsonObject: JSONObject , @HeaderMap hashMap: HashMap<String, String>): Call<List<CompletedSession>>

}