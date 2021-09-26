package com.upmyranksapp.adapter

import com.upmyranksapp.model.MOCKTEST
import com.upmyranksapp.model.onBoarding.AttemptedTest

interface TestClickListener {
    fun onTestClicked(isClicked : Boolean,mockTest: MOCKTEST)
    fun onResultClicked(id : String)
    fun onResultClicked(attempt :Int, studentId : String, testPaperId: String)
    fun onReviewClicked(attempt : AttemptedTest)
}