package com.trisys.rn.baseapp.utils

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig

object URLHelper {
    val mRemoteConfig = Firebase.remoteConfig
    private var baseURL = mRemoteConfig.getString(Define.BASE_URL)
    private var baseBATH = mRemoteConfig.getString(Define.BASE_PATH)
    private var url = baseURL+baseBATH


    private val baseURLSession = url + "session/"
    val getSessions = baseURLSession + "getSessions"
    val baseURLAuth = url + "auth/"
    val testBaseUrl = url + "testPaperAssign/"
    val averageBatchTests = testBaseUrl + "averageBatchTests"
    val unattemptedTests = testBaseUrl + "attemptedTests"
    val logout = baseURLAuth + "logout"
}