package com.segmentify.segmentifyandroidsdk.network.Factories

import com.segmentify.segmentifyandroidsdk.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query

interface PushFactory {

        // Gimli returns text/plain (not JSON) on success/error.
        @POST("/native/subscription/push")
        fun sendNotification(@Body notificationModel: NotificationModel,@Query("apiKey")apiKey : String): Call<ResponseBody>

        @POST("/native/interaction/notification")
        fun sendNotificationInteraction(@Body notificationModel: NotificationModel,@Query("apiKey")apiKey : String): Call<ResponseBody>

}
