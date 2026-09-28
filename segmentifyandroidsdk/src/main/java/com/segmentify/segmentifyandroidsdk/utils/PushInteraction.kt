package com.segmentify.segmentifyandroidsdk.utils

object PushInteraction {

    fun resolveInteractionId(instanceId: String, interactionId: String?): String {
        if (interactionId.isNullOrBlank()) return instanceId
        return interactionId
    }
}
