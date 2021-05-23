package com.trisys.rn.baseapp

import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.GzipRequestInterceptor
import okhttp3.OkHttpClient

class MyApplication: MultiDexApplication() {
    var mInstance: MyApplication? = null
    override fun onCreate() {
        super.onCreate()
        mInstance = this;

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(GzipRequestInterceptor())
            .build()


        AndroidNetworking.initialize(getApplicationContext(),okHttpClient);
        MultiDex.install(this)
    }

}