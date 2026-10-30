package com.segmentify.segmentifyandroidsdk

import com.google.gson.JsonParser
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushAction
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class SegmentifyPushPayloadTest {

    private fun fixture(name: String): Map<String, String> {
        val stream = javaClass.classLoader!!.getResourceAsStream("push-fixtures/$name")
        val json = JsonParser.parseString(stream.bufferedReader().use { it.readText() }).asJsonObject
        return json.entrySet()
            .filter { it.value.isJsonPrimitive }
            .associate { it.key to it.value.asString }
    }

    @Test
    fun parsesV2AndLegacyIdentically() {
        val legacy = SegmentifyPushPayload.from(fixture("legacy-android.json"))
        val v2 = SegmentifyPushPayload.from(fixture("v2-full.json"))

        assertEquals(legacy, v2.copy(sgmVersion = null, tag = null, userId = null))
        assertEquals("2", v2.sgmVersion)
        assertEquals("summer-sale", v2.tag)
        assertEquals("user-1", v2.userId)
    }

    @Test
    fun parsesLegacyKeys() {
        val payload = SegmentifyPushPayload.from(fixture("legacy-android.json"))

        assertNull(payload.sgmVersion)
        assertFalse(payload.isV2)
        assertEquals("Summer sale", payload.title)
        assertEquals("Up to 50% off", payload.message)
        assertEquals("https://cdn.example.com/sale.png", payload.image)
        assertEquals("https://shop.example.com/sale", payload.redirectUrl)
        assertEquals("app://sale-android", payload.deeplink)
        assertEquals("ins-1", payload.instanceId)
        assertEquals("api-key", payload.apiKey)
        assertEquals("https://dc.example.com", payload.dataCenterUrl)
        assertEquals(
            listOf(SegmentifyPushAction(action = "action0", title = "Open", redirectUrl = "https://shop.example.com/open")),
            payload.actions
        )
    }

    @Test
    fun v2PayloadIsMarkedAsV2() {
        assertTrue(SegmentifyPushPayload.from(fixture("v2-full.json")).isV2)
    }

    @Test
    fun unknownKeysAreIgnored() {
        val withUnknown = fixture("v2-full.json") + mapOf("futureKey" to "value")

        assertEquals(SegmentifyPushPayload.from(fixture("v2-full.json")), SegmentifyPushPayload.from(withUnknown))
    }

    @Test
    fun malformedActionsGiveEmptyList() {
        val payload = SegmentifyPushPayload.from(fixture("malformed-actions.json"))

        assertEquals(emptyList<SegmentifyPushAction>(), payload.actions)
        assertEquals("Summer sale", payload.title)
    }

    @Test
    fun missingActionsGiveEmptyList() {
        assertEquals(emptyList<SegmentifyPushAction>(), SegmentifyPushPayload.from(mapOf("title" to "Sale")).actions)
    }

    @Test
    fun emptyMapDoesNotThrow() {
        val payload = SegmentifyPushPayload.from(fixture("empty.json"))

        assertEquals("", payload.title)
        assertEquals("", payload.message)
        assertNull(payload.instanceId)
        assertNull(payload.targetUrl)
        assertEquals(emptyList<SegmentifyPushAction>(), payload.actions)
    }

    @Test
    fun emptyV2ValuesAreTreatedAsMissing() {
        val payload = SegmentifyPushPayload.from(fixture("v2-minimal.json"))

        assertNull(payload.instanceId)
        assertNull(payload.deeplink)
        assertNull(payload.tag)
        assertNull(payload.targetUrl)
        assertEquals(emptyList<SegmentifyPushAction>(), payload.actions)
    }

    @Test
    fun deeplinkIsTheTargetUrl() {
        assertEquals("app://sale-android", SegmentifyPushPayload.from(fixture("v2-full.json")).targetUrl)
    }

    @Test
    fun emptyDeeplinkFallsBackToRedirectUrl() {
        val payload = SegmentifyPushPayload.from(fixture("v2-full.json") + mapOf("deeplink" to ""))

        assertEquals("https://shop.example.com/sale", payload.targetUrl)
    }
}
