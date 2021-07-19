package com.trisys.rn.baseapp.adapter

import com.trisys.rn.baseapp.model.MOCKTEST

interface TestClickListener {
    fun onTestClicked(isClicked : Boolean,mockTest: MOCKTEST)
    fun onResultClicked(id : String)
    fun onResultClicked(attempt :Int, studentId : String, testPaperId: String)
}