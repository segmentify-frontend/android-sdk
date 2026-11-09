package com.segmentify.segmentifyandroidsdk

import com.segmentify.segmentifyandroidsdk.model.NotificationType
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload
import com.segmentify.segmentifyandroidsdk.utils.Constant
import com.segmentify.segmentifyandroidsdk.utils.PushInteraction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PushInteractionTest {

    @Test
    fun suppliedInteractionIdIsUsed() {
        assertEquals("static", PushInteraction.resolveInteractionId("psh_abc", "static"))
    }

    @Test
    fun missingInteractionIdFallsBackToInstanceId() {
        assertEquals("psh_abc", PushInteraction.resolveInteractionId("psh_abc", null))
    }

    @Test
    fun blankInteractionIdFallsBackToInstanceId() {
        assertEquals("psh_abc", PushInteraction.resolveInteractionId("psh_abc", " "))
    }

    @Test
    fun pushStepIsPushNotClick() {
        assertEquals("push", Constant.pushStep)
        assertNotEquals(Constant.clickStep, Constant.pushStep)
    }

    @Test
    fun permissionInfoContainsSdkVersion() {
        assertEquals(BuildConfig.SDK_VERSION, PushInteraction.registrationSdkVersion(NotificationType.PERMISSION_INFO))
    }

    @Test
    fun interactionsDoNotCarrySdkVersion() {
        assertNull(PushInteraction.registrationSdkVersion(NotificationType.VIEW))
        assertNull(PushInteraction.registrationSdkVersion(NotificationType.CLICK))
    }

    private fun recorded(call: (SegmentifyPushPayload, (NotificationType, String) -> Unit) -> Unit, payload: SegmentifyPushPayload): List<Pair<NotificationType, String>> {
        val sent = mutableListOf<Pair<NotificationType, String>>()
        call(payload) { type, instanceId -> sent.add(type to instanceId) }
        return sent
    }

    @Test
    fun pushReceivedSendsViewWithPayloadInstanceId() {
        val payload = SegmentifyPushPayload.from(mapOf("instanceId" to "ins-1", "sgmVersion" to "2"))

        assertEquals(listOf(NotificationType.VIEW to "ins-1"), recorded(PushInteraction::onPushReceived, payload))
    }

    @Test
    fun pushClickedSendsClickWithPayloadInstanceId() {
        val payload = SegmentifyPushPayload.from(mapOf("instanceId" to "ins-1"))

        assertEquals(listOf(NotificationType.CLICK to "ins-1"), recorded(PushInteraction::onPushClicked, payload))
    }

    @Test
    fun nothingIsSentWithoutInstanceId() {
        val missing = SegmentifyPushPayload.from(emptyMap())
        val blank = SegmentifyPushPayload.from(mapOf("instanceId" to "", "sgmVersion" to "2"))

        assertTrue(recorded(PushInteraction::onPushReceived, missing).isEmpty())
        assertTrue(recorded(PushInteraction::onPushClicked, missing).isEmpty())
        assertTrue(recorded(PushInteraction::onPushReceived, blank).isEmpty())
        assertTrue(recorded(PushInteraction::onPushClicked, blank).isEmpty())
    }
}
