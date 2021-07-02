package com.trisys.rn.baseapp.network

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.trisys.rn.baseapp.utils.Define

object URLHelper {
    val mRemoteConfig = Firebase.remoteConfig
    private var baseURL = mRemoteConfig.getString(Define.BASE_URL)
    private var baseBATH = mRemoteConfig.getString(Define.BASE_PATH)
    private var url = baseURL+baseBATH


    private val baseURLSession = url + "session/"
    val getSessions = baseURLSession + "getSessions"
    val baseURLAuth = url + "auth/"
    val testBaseUrl = url + "testPaperAssign/"
    val testResultUrl = url + "studentTestPaperAnswer/myTestResultList"
    val averageBatchTests = testBaseUrl + "averageBatchTests"
    val unattemptedTests = testBaseUrl + "unattemptedTests"
    val attemptedTests = testBaseUrl + "attemptedTests"
    val scheduleTestsForStudent = testBaseUrl + "scheduleTestsForStudent"
    val logout = baseURLAuth + "logout"
}