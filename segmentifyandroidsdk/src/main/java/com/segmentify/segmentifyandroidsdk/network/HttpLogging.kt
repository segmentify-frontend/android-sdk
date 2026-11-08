package com.segmentify.segmentifyandroidsdk.network

import com.segmentify.segmentifyandroidsdk.SegmentifyManager
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Holds the HTTP logging interceptor separately from ConnectionManager, so the
 * log level can be changed (e.g. by SegmentifyManager.logStatus) before config()
 * without initialising ConnectionManager, which needs the API URL.
 */
internal object HttpLogging {

    // Added to the client before AuthInterceptor, so the Authorization header
    // and apiKey query parameter are never part of what gets logged.
    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor()

    fun update() {
        interceptor.level = if (SegmentifyManager.clientPreferences?.isLogVisible() == true) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}
