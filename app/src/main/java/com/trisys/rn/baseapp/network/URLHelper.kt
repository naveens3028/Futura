package com.trisys.rn.baseapp.network

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.trisys.rn.baseapp.utils.Define

object URLHelper {
    /*private const val baseURL = "http://65.2.90.171/app/api/v1/"
    private const val baseURL = "https://api.upmyranks.com/app/api/v1/"*/

    val mRemoteConfig = Firebase.remoteConfig
    private var baseURL = mRemoteConfig.getString(Define.BASE_URL)
    private var baseBATH = mRemoteConfig.getString(Define.BASE_PATH)
    private var url = baseURL + baseBATH

    private val baseURLSession = url + "session/"
    private val testPaperAssign = url + "testPaperAssign/"
    val getSessions = baseURLSession + "getSessions"
    val baseURLAuth = url + "auth/"
    val averageBatchTests = url + "testPaperAssign/averageBatchTests"
    val scheduleTestsForStudent = testPaperAssign + "scheduleTestsForStudent"
    val logout = baseURLAuth + "logout"
}