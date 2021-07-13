package com.trisys.rn.baseapp.adapter

import com.trisys.rn.baseapp.model.MergedTest

interface TestClickListener {
    fun onTestClicked(isClicked : Boolean,mergedTest: MergedTest)
    fun onResultClicked(isClicked : Boolean)
}