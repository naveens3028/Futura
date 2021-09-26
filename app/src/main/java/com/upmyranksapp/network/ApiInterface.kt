package com.upmyranksapp.network

import com.upmyranksapp.model.onBoarding.CompletedSession
import retrofit2.Call
import retrofit2.http.POST

interface ApiInterface {

    @POST
    fun getData(): Call<List<CompletedSession>>

}