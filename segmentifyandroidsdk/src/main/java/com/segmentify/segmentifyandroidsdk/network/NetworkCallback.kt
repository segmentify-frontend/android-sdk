package com.segmentify.segmentifyandroidsdk.network

import com.segmentify.segmentifyandroidsdk.utils.SegmentifyLogger
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class NetworkCallback<T> : Callback<T> {

    private val TOTAL_RETRIES = 3
    private var retryCount = 0

    override fun onResponse(call: Call<T>?, response: Response<T>?) {
        response?.apply {
            if(isSuccessful)
            {
                val body = response.body()
                if(body != null){
                    onSuccess(body)
                } else {
                    SegmentifyLogger.printErrorLog("Request successful but body is null")
                    onError("Request successful but body is null")
                }
            }
            else if(response.code() != 200){
                if(call != null && !call.isCanceled) run {
                    SegmentifyLogger.printErrorLog(response.message())
                    if (retryCount++ < TOTAL_RETRIES) {
                        SegmentifyLogger.printErrorLog(": Retrying... ($retryCount out of $TOTAL_RETRIES)")
                        retry(call)
                    } else {
                        val errorBody = try { errorBody()?.string() } catch (e: Exception) { null }
                        onError("HTTP ${code()}" + if (errorBody.isNullOrBlank()) "" else ": $errorBody")
                    }
                }
            }
        }
    }

    abstract fun onSuccess(response:T)

    /** Called once when the request has finally failed (after any retries). */
    open fun onError(message: String) {}

    override fun onFailure(call: Call<T>, t: Throwable) {
        SegmentifyLogger.printErrorLog("Request failed: " + t.message)
        onError("Request failed: " + t.message)
    }

    private fun retry(call: Call<T>) {
        call.clone().enqueue(this)
    }

}