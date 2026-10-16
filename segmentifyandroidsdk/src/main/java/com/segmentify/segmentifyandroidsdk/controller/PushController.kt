package com.segmentify.segmentifyandroidsdk.controller

import com.google.gson.Gson
import com.segmentify.segmentifyandroidsdk.SegmentifyManager
import com.segmentify.segmentifyandroidsdk.model.NotificationModel
import com.segmentify.segmentifyandroidsdk.network.ConnectionManager
import com.segmentify.segmentifyandroidsdk.network.NetworkCallback
import com.segmentify.segmentifyandroidsdk.utils.ClientPreferences
import com.segmentify.segmentifyandroidsdk.utils.SegmentifyLogger
import okhttp3.ResponseBody
import java.lang.Exception


internal object PushController {

    var clientPreferences: ClientPreferences? = null

    fun sendNotification(notificationModel: NotificationModel) {

        try {
            if (SegmentifyManager.clientPreferences?.isLogVisible()!!) {
                SegmentifyLogger.printSuccessLog("Notification Payload : " + Gson().toJson(notificationModel))
            }

            ConnectionManager.getPushFactory().sendNotification(notificationModel)
                    .enqueue(object : NetworkCallback<ResponseBody>() {
                        override fun onSuccess(response: ResponseBody) {
                            if (SegmentifyManager.clientPreferences?.isLogVisible()!!) {
                                SegmentifyLogger.printSuccessLog("Notification sent successfully")
                            }
                            response.close()
                        }
                    })
        } catch (e: Exception) {
            SegmentifyLogger.printErrorLog("Error occurred when sending notification model, error: $e")
        }
    }

    fun sendNotificationInteraction(notificationModel: NotificationModel) {

        try {
            if (SegmentifyManager.clientPreferences?.isLogVisible()!!) {
                SegmentifyLogger.printSuccessLog("Notification Interaction Payload : " + Gson().toJson(notificationModel))
            }

            ConnectionManager.getPushFactory().sendNotificationInteraction(notificationModel)
                    .enqueue(object : NetworkCallback<ResponseBody>() {
                        override fun onSuccess(response: ResponseBody) {
                            if (SegmentifyManager.clientPreferences?.isLogVisible()!!) {
                                SegmentifyLogger.printSuccessLog("Notification interaction sent successfully")
                            }
                            response.close()
                        }
                    })
        } catch (e: Exception) {
            SegmentifyLogger.printErrorLog("Error occurred when sending notification interaction model, error: $e")
        }
    }
}
