package com.trisys.rn.baseapp.network

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.BuildConfig
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import com.trisys.rn.baseapp.network.ApiUtils.getAuthorizationHeader
import com.trisys.rn.baseapp.utils.Define
import com.trisys.rn.baseapp.utils.MyPreferences
import com.trisys.rn.baseapp.utils.Utils
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


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
        type:Int,
        onNetworkResponse: OnNetworkResponse
    ) {
        Utils.log(TAG, "url $url")
        Utils.log(TAG, "params $params")
        if (cd.isConnectingToInternet()) {

            val header = HashMap<String, String>()
            header.put("Accept", "application/json")
            header.put("Accept-Encoding","gzip, deflate")
            header.put("Connection", "keep-alive")
            header["Content-Type"] = "application/json; charset=utf-8"
            header["access_token"] = MyPreferences(context).getString(Define.ACCESS_TOKEN)!!
            header["Accept-Language"] = "en-US,en;q=0.9"
            header["User-Agent"] = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.106 Safari/537.36"
            if (callType == GET) {
                getCall(url, params, priority, tag, header,type, onNetworkResponse)
            } else {
                postCall(url, params, priority, tag,header,type, onNetworkResponse)
            }
        } else {
            onNetworkResponse.onNetworkResponse(responseNoInternet, "No Internet Connection..", tag)
        }
    }

    fun getCall(url: String, params:Map<String,String>, priority: Priority, tag:String, header:Map<String,String>,type:Int, onNetworkResponse: OnNetworkResponse){

        Utils.log("Url", url)
        Utils.log("params", params.toString())
        Utils.log("headers", header.toString())
        if(type == RESTYPE_OBJECT) {
            AndroidNetworking.get(url)
                .addQueryParameter(params)
                .addHeaders(header)
                .setTag(tag)
                .doNotCacheResponse()
                .setPriority(priority)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        // do anything with response
                        Log.e("Response", response.toString())
                        if (context != null)
                            onNetworkResponse.onNetworkResponse(responseSuccess,response.toString(),tag)
                    }

                    override fun onError(error: ANError) {
                        // handle error
                        Log.e("NetworkError", error.errorBody!!)
                        if (BuildConfig.DEBUG) {
                            val response = "URL :" + url + "\nError Code : " + error.errorCode + "response : \n" + error.errorDetail
                            onNetworkResponse.onNetworkResponse(responseFailed, response, tag)
                        } else {

                            if (context != null)
                                onNetworkResponse.onNetworkResponse(responseFailed,error.errorDetail,tag)
                        }
                    }
                })
        }else {

            AndroidNetworking.get(url)

                .addQueryParameter(params)
                //.addPathParameter(pathParams)
                .addHeaders(header)
                .setTag(tag)
                //.setContentType("application/json; charset=utf-8")
                .doNotCacheResponse()
                .setPriority(priority)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        // do anything with response

                         Utils.log("response", response.toString())
                        if (context != null)
                            onNetworkResponse.onNetworkResponse(responseSuccess,response.toString(),tag)
                    }

                    override fun onError(error: ANError) {
                        Utils.log("NetworkError", error.errorDetail.toString())
                        val response ="Error Code : " + error.errorCode + " " + error.errorDetail

                        if(error.errorCode == 0) {
                            onNetworkResponse.onNetworkResponse(responseFailed,response,tag)
                        }else{
                            onNetworkResponse.onNetworkResponse(responseFailed,response,tag)
                        }
                    }
                })
        }

    }
    fun postCall(url: String, params:Map<String,String>, priority: Priority, tag:String, header:Map<String,String>,type:Int, onNetworkResponse: OnNetworkResponse){
        Utils.log("Url", url)
        Utils.log("params", params.toString())
        if(type == RESTYPE_OBJECT) {
            AndroidNetworking.post(url)
                .addBodyParameter(params)
                .addHeaders(header)
                .setContentType("application/json; charset=utf-8")
                .setTag(tag)
                .setPriority(priority)
                .build()
                .getAsJSONObject(object : JSONObjectRequestListener {
                    override fun onResponse(response: JSONObject) {
                        if(context != null)
                            onNetworkResponse.onNetworkResponse(responseSuccess,response.toString(),tag)
                    }
                    override fun onError(error: ANError) {
                        Log.e("NetworkError",error.toString())
                        if(context != null)
                            if(error.errorDetail.equals("connectionError")){
                                onNetworkResponse.onNetworkResponse(responseNoInternet, "No Internet Connection..", tag)
                            }else {
                                onNetworkResponse.onNetworkResponse(responseFailed, error.errorDetail, tag)
                            }
                    }
                })
        }else {
            AndroidNetworking.post(url)

                .addQueryParameter(params)
                //.addPathParameter(pathParams)
                .addHeaders(header)
                .setTag(tag)
                .doNotCacheResponse()
                .setPriority(priority)
                .build()
                .getAsJSONArray(object : JSONArrayRequestListener {
                    override fun onResponse(response: JSONArray) {
                        Utils.log("response", response.toString())
                        if (context != null)
                            onNetworkResponse.onNetworkResponse(responseSuccess,response.toString(),tag)
                    }

                    override fun onError(error: ANError) {
                        Utils.log("NetworkError", error.errorDetail.toString())
                        val response = "Error Code : " + error.errorCode + " " + error.errorDetail

                        if (error.errorCode == 0) {
                            onNetworkResponse.onNetworkResponse(responseFailed, response, tag)
                        } else {
                            onNetworkResponse.onNetworkResponse(responseFailed, response, tag)
                        }
                    }
                })

        }
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

}