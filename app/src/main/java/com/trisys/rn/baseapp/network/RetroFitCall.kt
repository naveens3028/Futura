package com.trisys.rn.baseapp.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitCall {

    lateinit var retrofit: Retrofit
    private val BASE_URL = URLHelper.productionUrl

    fun retroFitCall() {

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}