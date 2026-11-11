package com.segmentify.segmentifyandroidsdk.utils

import com.segmentify.segmentifyandroidsdk.BuildConfig
import com.segmentify.segmentifyandroidsdk.model.NotificationType
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload

object PushInteraction {

    /** Key push-sender uses for the send request's correlationId in the push data. */
    const val CORRELATION_ID_KEY = "correlationId"

    /** Reads the correlationId from a Segmentify push's data map (RemoteMessage.getData()). */
    @JvmStatic
    fun correlationIdFrom(data: Map<String, String?>?): String? = normalizeCorrelationId(data?.get(CORRELATION_ID_KEY))

    /** push-sender sends "" when a push has no correlationId; treat blank as absent so it is not reported. */
    @JvmStatic
    fun normalizeCorrelationId(value: String?): String? = if (value.isNullOrBlank()) null else value

    fun resolveInteractionId(instanceId: String, interactionId: String?): String {
        if (interactionId.isNullOrBlank()) return instanceId
        return interactionId
    }

    fun registrationSdkVersion(type: NotificationType?): String? {
        if (type != NotificationType.PERMISSION_INFO) return null
        return BuildConfig.SDK_VERSION
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
