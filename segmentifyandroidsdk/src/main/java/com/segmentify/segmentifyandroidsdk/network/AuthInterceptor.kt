package com.segmentify.segmentifyandroidsdk.network

import com.segmentify.segmentifyandroidsdk.model.ConfigModel
import com.segmentify.segmentifyandroidsdk.utils.SegmentifyLogger
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the common headers and the authentication to every SDK request.
 *
 * - authHeader set: "Authorization: <authHeader>" exactly as configured (e.g. "Basic <token>"),
 *   and no apiKey query parameter
 *   (Unifonic push-notification-api)
 * - otherwise:      "?apiKey=<apiKey>" as in the original Segmentify setup
 */
internal class AuthInterceptor(private val configProvider: () -> ConfigModel?) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val config = try {
            configProvider()
        } catch (e: Exception) {
            SegmentifyLogger.printErrorLog("Could not read SDK config, request is sent without authentication: $e")
            null
        }

        val builder = request.newBuilder()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")

        val subDomain = config?.subDomain
        if (!subDomain.isNullOrEmpty()) {
            builder.header("Origin", subDomain)
        }

        val authorization = authorizationValue(config?.authHeader)
        val apiKey = config?.apiKey
        if (authorization != null) {
            builder.header("Authorization", authorization)
        } else if (!apiKey.isNullOrEmpty()) {
            builder.url(request.url().newBuilder().addQueryParameter("apiKey", apiKey).build())
        }

        return chain.proceed(builder.build())
    }

    companion object {
        /**
         * Returns the Authorization header value: the configured authHeader exactly
         * as given, with no scheme added or removed (so pass e.g. "Basic <token>").
         * Null or blank values return null, so the apiKey query parameter is used.
         */
        @JvmStatic
        fun authorizationValue(authHeader: String?): String? {
            if (authHeader.isNullOrBlank()) return null
            return authHeader
        }
    }
}
