package com.segmentify.segmentifyandroidsdk.model

import com.google.gson.annotations.SerializedName
import com.segmentify.segmentifyandroidsdk.BuildConfig
import com.segmentify.segmentifyandroidsdk.SegmentifyManager

open class SegmentifyObject{
    var userId : String? = null
        get() = field ?: SegmentifyManager.clientPreferences?.getUserId()
        set(value) { field = value }

    var sessionId : String? = null
        get() = field ?: SegmentifyManager.clientPreferences?.getSessionId()
        set(value) { field = value }

    /**
     * Materializes userId and sessionId from preferences into backing fields
     * so that Gson (which reads fields via reflection, not getters) includes them.
     */
    fun resolveIds() {
        if (userId == null) userId = SegmentifyManager.clientPreferences?.getUserId()
        if (sessionId == null) sessionId = SegmentifyManager.clientPreferences?.getSessionId()
    }

    @SerializedName("name")
    var eventName : String? = null

    var pageUrl : String? = null
    var currency : String? = null
    var lang : String? = null
    var params : Map<String,Object>? = null
    var nextPage : Boolean? = null
    var os : String? = "android"
    var device : String? = "android"
    var osversion : String? = android.os.Build.VERSION.RELEASE.toString()
    var advertisingIdentifier : String? = null
    var appVersion : String? = null
    var sdkVersion : String? = BuildConfig.SDK_VERSION
    var testMode:Boolean? = false
    var region : String? = ""
}