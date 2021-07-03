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
    private val testPaperAssign = url + "testPaperAssign/"
    private val testPaperVo = url + "testPaperVo/"
    private val studentAnswer = url + "studentAnswer/"
    private val studentTestPaperAnswer = url + "studentTestPaperAnswer/"
    val testResultUrl = studentTestPaperAnswer + "myTestResultList"
    val averageBatchTests = testPaperAssign + "averageBatchTests"
    val unattemptedTests = testPaperAssign + "unattemptedTests"
    val attemptedTests = testPaperAssign + "attemptedTests"
    val scheduleTestsForStudent = testPaperAssign + "scheduleTestsForStudent"
    val testPaperForStudent = testPaperVo + "testPaperForStudent"
    val getStudentTestPaper = testPaperVo + "getStudentTestPaper"
    val testStatus = studentAnswer + "testStatus"
    val submitTestPaper = studentTestPaperAnswer + "submitTestPaper"
    val next = studentAnswer + "next"
    val logout = baseURLAuth + "logout"
}