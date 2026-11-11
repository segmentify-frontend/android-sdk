package com.segmentify.segmentifyandroidsdk.utils

/**
 * Result of a push request such as SegmentifyManager.sendNotification.
 * Called on the main thread.
 */
interface PushResultCallback {
    /** The server accepted the request. */
    fun onSuccess()

    /** The request was not sent (e.g. missing deviceToken, SDK not configured) or the server rejected it. */
    fun onError(message: String)
}
