package com.trisys.rn.baseapp.network

import android.content.Context
import android.util.Log
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.BuildConfig
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.google.gson.Gson
import com.trisys.rn.baseapp.R
import com.trisys.rn.baseapp.utils.Utils
import org.json.JSONObject


class NetworkHelper(context: Context){

    var GET: Int = 1
    var POST: Int = 2
    var RESTYPE_OBJECT: Int = 1
    var RESTYPE_ARRAY: Int = 2

    var responseSuccess = 0
    var responseFailed = 1
    var responseNoInternet = 2

    lateinit var context: Context

    lateinit var cd: ConnectionDetector
    lateinit var gson:Gson

    init {
        if (context != null) {
            cd = ConnectionDetector(context)
            gson  = Gson()
            this.context = context

            AndroidNetworking.enableLogging();

        }
    }

    public fun call(callType:Int, url: String, params:Map<String,String>,priority: Priority,tag:String,onNetworkResponse: OnNetworkResponse){

        if(cd.isConnectingToInternet()) {

            val header  = HashMap<String, String>()
            header.put("Accept","application/json")
            header.put("Content-Type","application/json")
            //header.put("Accept-Encoding","gzip, deflate")
            header.put("Connection","keep-alive")
            if (callType == GET) {
                getCall(url, params, priority,tag,onNetworkResponse,header)
            } else {
                postCall(url, params,priority,tag,onNetworkResponse, header)
            }
        }else{
            onNetworkResponse.onNetworkResponse(responseNoInternet,"No Internet Connection..",tag)
        }
    }

    fun getCall(url: String, params:Map<String,String>, priority: Priority, tag:String, onNetworkResponse: OnNetworkResponse, headers:Map<String,String>){

        Utils.log("params", params.toString())
        Utils.log("url", url)
        Utils.log("header", headers.toString())

        AndroidNetworking.get(url)

            .addQueryParameter(params)
            .addHeaders(headers)
            .setTag(tag)
            //.setContentType("application/json; charset=utf-8")
            .doNotCacheResponse()
            .setPriority(priority)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject) {
                    // do anything with response

                    Utils.log("response", response.toString())
                    if (context != null)
                        onNetworkResponse.onNetworkResponse(
                            responseSuccess,
                            response.toString(),
                            tag
                        )
                }

                override fun onError(error: ANError) {
                    // handle error
                    Utils.log("NetworkError", error.errorDetail.toString())
                    val response ="Error Code : " + error.errorCode + " " + error.errorDetail
                    if (BuildConfig.DEBUG) {
                        onNetworkResponse.onNetworkResponse(
                            responseFailed,
                            response,
                            tag
                        )
                    } else {

                        if (context != null)
                            if(error.errorCode == 0) {
                                onNetworkResponse.onNetworkResponse(
                                    responseNoInternet,
                                    context.getString(R.string.errorCode0),
                                    tag
                                )
                            }else{
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
    fun postCall(url: String, params:Map<String,String>, priority: Priority, tag:String, onNetworkResponse: OnNetworkResponse, headers:Map<String,String>){

//        AndroidNetworking.post(url)
//            .addBodyParameter(params)
//                //.addApplicationJsonBody(jsonObject)
//                .addHeaders(headers)
//                .setOkHttpClient(OkHttpClient())
//                .setContentType("application/json; charset=utf-8")
//                .setTag(tag)
//                .setPriority(priority)
//                .build()
//                .getAsJSONObject(object : JSONObjectRequestListener {
//                    override fun onResponse(response: JSONObject) {
//                        // do anything with response
//
//                        if(context != null)
//                        onNetworkResponse.onNetworkResponse(responseSuccess,response.toString(),tag)
//                    }
//
//                    override fun onError(error: ANError) {
//                        // handle error
//                        Log.e("NetworkError",error.errorBody!!)
//                        if(context != null)
//                            if(error.errorDetail.equals("connectionError")){
//                                onNetworkResponse.onNetworkResponse(responseNoInternet, "No Internet Connection..", tag)
//                            }else {
//                                onNetworkResponse.onNetworkResponse(responseFailed, error.errorDetail, tag)
//                            }
//                    }
//                })

        val queue = Volley.newRequestQueue(context)
        val stringRequest = object: StringRequest(Request.Method.POST, url,
            Response.Listener<String> { response ->
                Log.d("A", "Response is: " + response.substring(0,500))
                if(context != null)
                    onNetworkResponse.onNetworkResponse(responseSuccess,response.toString(),tag)

            },
            Response.ErrorListener {
                if(context != null)
                    if(it.networkResponse.data.equals("connectionError")){
                        onNetworkResponse.onNetworkResponse(responseNoInternet, "No Internet Connection..", tag)
                    }else {
                        onNetworkResponse.onNetworkResponse(responseFailed, "Something went wrong!, Please try again..", tag)
                    }
            })
        {
            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers
            }
            override fun getBodyContentType(): String? {
                return "application/json"
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                return JSONObject(params).toString().toByteArray()
            }
        }
        queue.add(stringRequest).setTag(tag)

    }

    fun cancelAll(){
        AndroidNetworking.cancelAll()
    }
    fun cancel(tag: String){
        AndroidNetworking.cancel(tag)
    }

    fun cancelAllVolley(){
        val queue = Volley.newRequestQueue(context)
        queue.cancelAll(RequestQueue.RequestFilter { true })
    }
    fun cancelVolley(tag: String){
        val queue = Volley.newRequestQueue(context)
        queue.cancelAll(tag)
    }
}
