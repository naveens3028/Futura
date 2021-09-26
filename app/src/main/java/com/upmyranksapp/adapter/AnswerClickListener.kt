package com.upmyranksapp.adapter

interface AnswerClickListener {
    fun onAnswerClicked(isClicked: Boolean, option: Char, position: Int)
}