package com.trisys.rn.baseapp

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.interceptors.GzipRequestInterceptor
import okhttp3.OkHttpClient
import com.trisys.rn.baseapp.MyApplication as MyApplication1


class MyApplication: MultiDexApplication() {
    var mInstance: MyApplication1? = null

    override fun onCreate() {
        super.onCreate()
        mInstance = this

        //disable screenshot and Video recording all screens
        setupActivityListener()
        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(GzipRequestInterceptor())
            .build()


        AndroidNetworking.initialize(applicationContext,okHttpClient)
        MultiDex.install(this)
    }

    private fun setupActivityListener() {
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                activity.window.setFlags(
                    WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE
                )
            }
            override fun onActivityStarted(activity: Activity) {}
            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {}
            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}