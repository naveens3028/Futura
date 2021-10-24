package com.trisys.rn.baseapp.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.model.onBoarding.CompletedSession


class CompletedLiveActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compeleted_live)

        val data = Gson().fromJson(intent.getStringExtra("completedLive"), CompletedSession::class.java)

        Log.e("popCompleted", data.toString())

    }
}