package com.segmentify.segmentifyandroidsdk.model

import com.google.gson.annotations.SerializedName

enum class Channel {
    @SerializedName("app_push") APP_PUSH,
    @SerializedName("email") EMAIL,
    @SerializedName("whatsapp") WHATSAPP,
    @SerializedName("sms") SMS,
    @SerializedName("call") CALL
}
