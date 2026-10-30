package com.segmentify.segmentifyandroidsdk.model

import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser

data class SegmentifyPushAction(
    val action: String,
    val title: String = "",
    val icon: String? = null,
    val redirectUrl: String? = null,
    val deeplink: String? = null
)

data class SegmentifyPushPayload(
    val sgmVersion: String? = null,
    val title: String = "",
    val message: String = "",
    val image: String? = null,
    val redirectUrl: String? = null,
    val deeplink: String? = null,
    val tag: String? = null,
    val instanceId: String? = null,
    val apiKey: String? = null,
    val dataCenterUrl: String? = null,
    val userId: String? = null,
    val actions: List<SegmentifyPushAction> = emptyList()
) {

    val isV2: Boolean
        get() = sgmVersion != null

    val targetUrl: String?
        get() = deeplink ?: redirectUrl

    companion object {

        @JvmStatic
        fun from(data: Map<String, String?>): SegmentifyPushPayload {
            fun value(key: String): String? = data[key]?.takeIf { it.isNotBlank() }

            return SegmentifyPushPayload(
                sgmVersion = value("sgmVersion"),
                title = value("title") ?: "",
                message = value("message") ?: "",
                image = value("image"),
                redirectUrl = value("redirectUrl"),
                deeplink = value("deeplink"),
                tag = value("tag"),
                instanceId = value("instanceId"),
                apiKey = value("apiKey"),
                dataCenterUrl = value("dataCenterUrl"),
                userId = value("userId"),
                actions = parseActions(value("actions"))
            )
        }

        private fun parseActions(json: String?): List<SegmentifyPushAction> {
            if (json == null) return emptyList()
            val array = try {
                JsonParser.parseString(json)
            } catch (e: JsonParseException) {
                return emptyList()
            }
            if (!array.isJsonArray) return emptyList()
            return array.asJsonArray.mapNotNull { toAction(it) }
        }

        private fun toAction(element: JsonElement): SegmentifyPushAction? {
            if (!element.isJsonObject) return null
            val item = element.asJsonObject
            fun value(key: String): String? =
                item.get(key)?.takeIf { it.isJsonPrimitive }?.asString?.takeIf { it.isNotBlank() }

            val action = value("action") ?: return null
            return SegmentifyPushAction(
                action = action,
                title = value("title") ?: "",
                icon = value("icon"),
                redirectUrl = value("redirectUrl"),
                deeplink = value("deeplink")
            )
        }
    }
}
