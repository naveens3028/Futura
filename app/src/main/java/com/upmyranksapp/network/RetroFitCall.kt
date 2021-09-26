package com.upmyranksapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL

object RetroFitCall {

    lateinit var retrofit: Retrofit
    private val BASE_URL = URLHelper.getCompletedSessionsSubject

    fun retroFitCall() {

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}