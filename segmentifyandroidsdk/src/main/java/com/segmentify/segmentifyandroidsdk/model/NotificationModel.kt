package com.segmentify.segmentifyandroidsdk.model

import android.os.Build
import com.segmentify.segmentifyandroidsdk.SegmentifyManager
import com.segmentify.segmentifyandroidsdk.utils.PushInteraction

class NotificationModel{

    var deviceToken:String? = null
    var type:NotificationType? = null
    var instanceId:String? = null
    var interactionId:String? = null
    var sdkVersion:String? = null
    var correlationId:String? = null
    private  var userId : String?=null
    private  var osVersion:String?=null
    private  var os:String?=null
    private  var providerType:String?=null


    init {
        if(type != NotificationType.PERMISSION_INFO){
            deviceToken = null
        }

        osVersion = Build.VERSION.SDK_INT.toString()
        os = "ANDROID"
        providerType = "FIREBASE"

        var userId_ = SegmentifyManager.clientPreferences?.getUserId()

        if(!userId_.isNullOrEmpty()){
            userId = userId_
        }

    }

    companion object {
        /**
         * Builds a VIEW/CLICK model from a Segmentify push's data map (RemoteMessage.getData()),
         * copying instanceId and correlationId so shown/clicked can be tied to the send request.
         */
        @JvmStatic
        fun fromPushData(data: Map<String, String?>?, type: NotificationType): NotificationModel {
            val model = NotificationModel()
            model.type = type
            model.instanceId = data?.get("instanceId")
            model.correlationId = PushInteraction.correlationIdFrom(data)
            return model
        }
    }

}