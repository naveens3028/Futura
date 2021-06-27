package com.trisys.rn.baseapp.network

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.google.gson.Gson
import com.trisys.rn.baseapp.network.ApiUtils.getAuthorizationHeader
import com.trisys.rn.baseapp.utils.Utils
import org.json.JSONObject
import java.util.*

enum class RequestType {
    GET, POST_WITHOUT_AUTH, POST_WITH_AUTH
}

class NetworkHelper(context: Context) {

    var GET: Int = 1
    var POST: Int = 2
    var RESTYPE_OBJECT: Int = 1
    var RESTYPE_ARRAY: Int = 2
    var TAG = NetworkHelper::class.java.simpleName

    var responseSuccess = 0
    var responseFailed = 1
    var responseNoInternet = 2

    lateinit var context: Context

    lateinit var cd: ConnectionDetector
    lateinit var gson: Gson

    init {
        if (context != null) {
            cd = ConnectionDetector(context)
            gson = Gson()
            this.context = context

            AndroidNetworking.enableLogging()

        }
    }

    fun call(
        callType: Int,
        url: String,
        params: Map<String, String>,
        priority: Priority,
        tag: String,
        onNetworkResponse: OnNetworkResponse
    ) {

        Utils.log(TAG, "url $url")
        Utils.log(TAG, "params $params")
        if (cd.isConnectingToInternet()) {

            val header = HashMap<String, String>()

            if (callType == GET) {
                getCall(url, params, priority, tag, onNetworkResponse, header)
            } else {
                postCall(url, params, priority, tag, onNetworkResponse, header)
            }
        } else {
            onNetworkResponse.onNetworkResponse(responseNoInternet, "No Internet Connection..", tag)
        }
    }

    private fun postCall(
        url: String,
        params: Map<String, String>,
        priority: Priority,
        tag: String,
        onNetworkResponse: OnNetworkResponse,
        headers: Map<String, String>
    ) {

        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.POST, url, Response.Listener { response ->

            Utils.log(TAG, "Response ${response.toString()} k")
            onNetworkResponse.onNetworkResponse(responseSuccess, response.toString(), tag)

        },
            Response.ErrorListener {
                Utils.log(
                    TAG,
                    "Network error ${it.networkResponse.data} ${it.networkResponse.statusCode}"
                )

                if (it.networkResponse.data.equals("connectionError")) {
                    onNetworkResponse.onNetworkResponse(
                        responseNoInternet,
                        "No Internet Connection..",
                        tag
                    )
                } else {
                    onNetworkResponse.onNetworkResponse(
                        responseFailed,
                        "Something went wrong!, Please try again..",
                        tag
                    )
                }
            }) {

            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
//                headers["access_token"] = "72640d71-abab-4db5-bfa9-d9f4ca0ebcd0"
                return headers
            }

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(stringRequest).tag = tag
    }

    fun loginPostCall(
        url: String,
        params: Map<String, String>,
        priority: Priority,
        tag: String,
        onNetworkResponse: OnNetworkResponse,
    ) {

        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->

                Utils.log(TAG, "Response ${response.toString()} k")
                onNetworkResponse.onNetworkResponse(responseSuccess, response.toString(), tag)

            },
            Response.ErrorListener {
                Utils.log(
                    TAG,
                    "Network error ${it.networkResponse.data} ${it.networkResponse.statusCode}"
                )

                if (it.networkResponse.data.equals("connectionError")) {
                    onNetworkResponse.onNetworkResponse(
                        responseNoInternet,
                        "No Internet Connection..",
                        tag
                    )
                } else {
                    onNetworkResponse.onNetworkResponse(
                        responseFailed,
                        "Something went wrong!, Please try again..",
                        tag
                    )
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return getAuthorizationHeader(context)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return JSONObject(params).toString().toByteArray()
            }
        }
        queue.add(stringRequest).tag = tag
    }

    fun getCall(
        url: String,
        params: Map<String, String>,
        priority: Priority,
        tag: String,
        onNetworkResponse: OnNetworkResponse,
        headers: Map<String, String>
    ) {

        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(Method.GET, url, Response.Listener { response ->

            Utils.log(TAG, "Response $response")
            onNetworkResponse.onNetworkResponse(responseSuccess, response.toString(), tag)

        },
            Response.ErrorListener {
                Utils.log(
                    TAG,
                    "Network error ${it.networkResponse.data} ${it.networkResponse.statusCode}"
                )

                if (it.networkResponse.data.equals("connectionError")) {
                    onNetworkResponse.onNetworkResponse(
                        responseNoInternet,
                        "No Internet Connection..",
                        tag
                    )
                } else {
                    onNetworkResponse.onNetworkResponse(
                        responseFailed,
                        "Something went wrong!, Please try again..",
                        tag
                    )
                }
            }) {
            override fun getHeaders(): MutableMap<String, String> {
                return getAuthorizationHeader(context)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }

            @Throws(AuthFailureError::class)
            override fun getParams(): Map<String, String> {
                return params
            }
        }
        queue.add(stringRequest).tag = tag
    }
//
//    fun cancelAll() {
//        AndroidNetworking.cancelAll()
//    }
//
//    fun cancel(tag: String) {
//        AndroidNetworking.cancel(tag)
//    }

    fun cancelAllVolley() {
        val queue = Volley.newRequestQueue(context)
        queue.cancelAll(RequestQueue.RequestFilter { true })
    }

    fun cancelVolley(tag: String) {
        val queue = Volley.newRequestQueue(context)
        queue.cancelAll(tag)
    }
}