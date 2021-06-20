package com.trisys.rn.baseapp.network

import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.BuildConfig
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.network.ApiUtils.getAuthorizationHeader
import com.trisys.rn.baseapp.network.ApiUtils.getHeader
import com.trisys.rn.baseapp.utils.Utils
import org.json.JSONObject

enum class RequestType {
    GET, POST_WITHOUT_AUTH, POST_WITH_AUTH
}

class NetworkHelper(var context: Context) {

    var kTAG: String = NetworkHelper::class.java.simpleName

    var responseSuccess = 0
    var responseFailed = 1
    var responseNoInternet = 2

    var cd: ConnectionDetector = ConnectionDetector(context)

    fun call(
        callType: RequestType,
        url: String,
        params: Map<String, String>,
        tag: String,
        onNetworkResponse: OnNetworkResponse
    ) {

        if (cd.isConnectingToInternet()) {

            when (callType) {
                RequestType.GET -> {
                    getCall(url, params, tag, onNetworkResponse, getAuthorizationHeader(context))
                }
                RequestType.POST_WITHOUT_AUTH -> {
                    postCall(url, params, tag, onNetworkResponse, getHeader())
                }
                RequestType.POST_WITH_AUTH -> {
                    postCall(url, params, tag, onNetworkResponse, getAuthorizationHeader(context))
                }
            }
        } else {
            onNetworkResponse.onNetworkResponse(responseNoInternet, "No Internet Connection..", tag)
        }
    }

    private fun getCall(
        url: String,
        params: Map<String, String>,
        tag: String,
        onNetworkResponse: OnNetworkResponse,
        headers: MutableMap<String, String>
    ) {
        Utils.log(kTAG, "url $url")
        Utils.log(kTAG, "header $headers")
        Utils.log(kTAG, "params $params")

        AndroidNetworking.get(url)
            .addQueryParameter(params)
            .addHeaders(headers)
            .setTag(tag)
            .doNotCacheResponse()
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    Utils.log(kTAG, "response $response")
                    onNetworkResponse.onNetworkResponse(
                        responseSuccess,
                        response.toString(),
                        tag
                    )
                }

                override fun onError(error: ANError) {
                    Utils.log(kTAG, "NetworkError ${error.errorDetail} ${error.errorCode}")
                    val response = "Error Code : " + error.errorCode + " " + error.errorDetail
                    if (BuildConfig.DEBUG) {
                        onNetworkResponse.onNetworkResponse(
                            responseFailed,
                            response,
                            tag
                        )
                    } else {
                        if (error.errorCode == 0) {
                            onNetworkResponse.onNetworkResponse(
                                responseNoInternet,
                                context.getString(R.string.errorCode0),
                                tag
                            )
                        } else {
                            onNetworkResponse.onNetworkResponse(
                                responseFailed,
                                context.getString(R.string.errorCode503),
                                tag
                            )
                        }
                    }
                }
            })

    }

    private fun postCall(
        url: String,
        params: Map<String, String>,
        tag: String,
        onNetworkResponse: OnNetworkResponse,
        headers: MutableMap<String, String>
    ) {

        Utils.log(kTAG, "url $url")
        Utils.log(kTAG, "header $headers")
        Utils.log(kTAG, "params $params")
        val queue = Volley.newRequestQueue(context)
        val stringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Utils.log(kTAG, "Response $response")
                onNetworkResponse.onNetworkResponse(responseSuccess, response.toString(), tag)
            },
            Response.ErrorListener {
                Utils.log(
                    kTAG,
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
                return headers
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

    fun cancelAll() {
        AndroidNetworking.cancelAll()
    }

    fun cancel(tag: String) {
        AndroidNetworking.cancel(tag)
    }

    fun cancelAllVolley() {
        val queue = Volley.newRequestQueue(context)
        queue.cancelAll(RequestQueue.RequestFilter { true })
    }

    fun cancelVolley(tag: String) {
        val queue = Volley.newRequestQueue(context)
        queue.cancelAll(tag)
    }
}