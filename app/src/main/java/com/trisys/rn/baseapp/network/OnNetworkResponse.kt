package com.trisys.rn.baseapp.network

interface OnNetworkResponse {
    fun onNetworkResponse(responseCode:Int, response:String, tag:String)
}