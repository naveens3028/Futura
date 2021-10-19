package com.trisys.rn.baseapp.network

import com.trisys.rn.baseapp.model.onBoarding.CompletedSession
import retrofit2.Call
import retrofit2.http.POST

interface ApiInterface {

    @POST
    fun getData(): Call<List<CompletedSession>>

}