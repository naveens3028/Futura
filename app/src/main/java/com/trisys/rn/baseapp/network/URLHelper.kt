package com.trisys.rn.baseapp.network

import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.trisys.rn.baseapp.utils.Define

object URLHelper {
    val mRemoteConfig = Firebase.remoteConfig
    private var baseURL = mRemoteConfig.getString(Define.BASE_URL)
    private var baseBATH = mRemoteConfig.getString(Define.BASE_PATH)

    //    private var url = baseURL+baseBATH
    private var url = "http://65.2.90.171/app/api/v1/"
//    private var url = "http://uatreactcode.s3-website.ap-south-1.amazonaws.com/app/api/v1/"
    private var productionUrl = "https://api.upmyranks.com/app/api/v1"


    private val baseURLSession = productionUrl + "session/"
    val getSessions = baseURLSession + "getSessions"
    val baseURLAuth = url + "auth/"
    val courseURL = url + "course/child/"
    private val testPaperAssign = url + "testPaperAssign/"
    private val testPaperVo = url + "testPaperVo/"
    private val studentAnswer = url + "studentAnswer/"
    private val studentTestPaperAnswer = url + "studentTestPaperAnswer/"
    private val material = url + "material/"
    val testResultUrl = studentTestPaperAnswer + "myTestResultList"
    val averageBatchTests = testPaperAssign + "averageBatchTests"
    val unattemptedTests = testPaperAssign + "unattemptedTests"
    val attemptedTests = testPaperAssign + "attemptedTests"
    val scheduleTestsForStudent = testPaperAssign + "scheduleTestsForStudent"
    val testPaperForStudent = testPaperVo + "testPaperForStudent"
    val getStudentTestPaper = testPaperVo + "getStudentTestPaper"
    val testStatus = studentAnswer + "testStatus"
    val submitTestPaper = studentTestPaperAnswer + "submitTestPaper"
    val answeredTestPaper = studentTestPaperAnswer + "answeredTestPaper"
    val next = studentAnswer + "next"
    val publishedMaterialsByChapter = material + "publishedMaterialsByChapter"
    val logout = baseURLAuth + "logout"
}