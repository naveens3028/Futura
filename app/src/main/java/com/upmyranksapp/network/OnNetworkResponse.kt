package com.upmyranksapp.network

interface OnNetworkResponse {
    fun onNetworkResponse(responseCode:Int, response:String, tag:String)
}