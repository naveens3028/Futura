package com.trisys.rn.baseapp.adapter

import com.trisys.rn.baseapp.model.MOCKTEST
import com.trisys.rn.baseapp.model.onBoarding.AttemptedTest

interface TestClickListener {
    fun onTestClicked(isClicked : Boolean,mockTest: MOCKTEST)
    fun onResultClicked(id : String)
    fun onResultClicked(attempt :Int, studentId : String, testPaperId: String)
    fun onReviewClicked(attempt : AttemptedTest)
}