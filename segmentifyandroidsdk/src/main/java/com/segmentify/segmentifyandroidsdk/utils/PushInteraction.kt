package com.segmentify.segmentifyandroidsdk.utils

import com.segmentify.segmentifyandroidsdk.BuildConfig
import com.segmentify.segmentifyandroidsdk.model.NotificationType

object PushInteraction {

    fun resolveInteractionId(instanceId: String, interactionId: String?): String {
        if (interactionId.isNullOrBlank()) return instanceId
        return interactionId
    }

    fun registrationSdkVersion(type: NotificationType?): String? {
        if (type != NotificationType.PERMISSION_INFO) return null
        return BuildConfig.SDK_VERSION
    }
}
