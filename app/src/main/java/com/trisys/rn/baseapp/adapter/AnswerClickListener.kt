package com.trisys.rn.baseapp.adapter

interface AnswerClickListener {
    fun onAnswerClicked(isClicked: Boolean, option: Char, position: Int)
}