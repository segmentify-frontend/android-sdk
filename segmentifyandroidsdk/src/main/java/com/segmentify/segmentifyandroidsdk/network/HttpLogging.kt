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
    // and apiKey query parameter are not part of what gets logged. The header is
    // also redacted, so it stays hidden even if the interceptor order changes.
    val interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        redactHeader("Authorization")
    }

    fun update() {
        interceptor.level = if (SegmentifyManager.clientPreferences?.isLogVisible() == true) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}
