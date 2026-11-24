package com.segmentify.segmentifyandroidsdk.utils

import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload

data class PushActionSpec(
    val actionId: String,
    val title: String,
    val target: String
) {

    companion object {

        const val MAX_ACTIONS = 2
        private const val ACTION_ID_PREFIX = "action"

        @JvmStatic
        fun from(payload: SegmentifyPushPayload): List<PushActionSpec> =
            payload.actions.mapIndexedNotNull { index, action ->
                val target = action.deeplink ?: payload.actionUrls.getOrNull(index) ?: action.redirectUrl
                if (action.title.isBlank() || target == null) null else action.title to target
            }.take(MAX_ACTIONS).mapIndexed { index, (title, target) ->
                PushActionSpec(ACTION_ID_PREFIX + index, title, target)
            }
    }
}
