package com.trisys.rn.baseapp.utils

object URLHelper {
    private const val baseURL = "http://65.2.90.171/app/api/v1/"
    private const val baseURLSession = baseURL + "session/"
    private const val baseURLAuth = baseURL + "auth/"
    const val getSessions = baseURLSession + "getSessions"
    const val logout = baseURLAuth + "logout"
}