package com.segmentify.segmentifyandroidsdk.utils

import com.segmentify.segmentifyandroidsdk.BuildConfig
import com.segmentify.segmentifyandroidsdk.model.NotificationType
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload

object PushInteraction {

    private const val ACTION_ID_PARAM = "actionId"

    fun resolveInteractionId(instanceId: String, interactionId: String?): String {
        if (interactionId.isNullOrBlank()) return instanceId
        return interactionId
    }

    fun registrationSdkVersion(type: NotificationType?): String? {
        if (type != NotificationType.PERMISSION_INFO) return null
        return BuildConfig.SDK_VERSION
    }

    fun registrationSupportsActions(type: NotificationType?, pushActionsEnabled: Boolean): Boolean? {
        if (type != NotificationType.PERMISSION_INFO || !pushActionsEnabled) return null
        return true
    }

    fun onPushActionClicked(payload: SegmentifyPushPayload, actionId: String, send: (NotificationType, String, Map<String, String>) -> Unit) {
        val instanceId = payload.instanceId ?: return
        send(NotificationType.CLICK, instanceId, mapOf(ACTION_ID_PARAM to actionId))
    }

    fun onPushReceived(payload: SegmentifyPushPayload, send: (NotificationType, String) -> Unit) {
        sendWithInstanceId(payload, NotificationType.VIEW, send)
    }

    fun onPushClicked(payload: SegmentifyPushPayload, send: (NotificationType, String) -> Unit) {
        sendWithInstanceId(payload, NotificationType.CLICK, send)
    }

    private fun sendWithInstanceId(payload: SegmentifyPushPayload, type: NotificationType, send: (NotificationType, String) -> Unit) {
        val instanceId = payload.instanceId ?: return
        send(type, instanceId)
    }
}
