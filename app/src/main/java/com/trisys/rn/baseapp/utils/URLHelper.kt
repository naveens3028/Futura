package com.trisys.rn.baseapp.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

object URLHelper {
    val mRemoteConfig = Firebase.remoteConfig
    private var baseURL = mRemoteConfig.getString(Define.BASE_URL)
    private var baseBATH = mRemoteConfig.getString(Define.BASE_PATH)
    private var url = baseURL+baseBATH


    val averageBatchTests = url + "testPaperAssign/averageBatchTests"
    private val baseURLSession = url + "session/"
    val baseURLAuth = url + "auth/"
    val getSessions = baseURLSession + "getSessions"
    val logout = baseURLAuth + "logout"
}