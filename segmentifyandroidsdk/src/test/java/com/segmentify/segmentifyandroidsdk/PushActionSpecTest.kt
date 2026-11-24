package com.segmentify.segmentifyandroidsdk

import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload
import com.segmentify.segmentifyandroidsdk.utils.PushActionSpec
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PushActionSpecTest {

    private fun payloadWithActions(actionsJson: String): SegmentifyPushPayload =
        SegmentifyPushPayload.from(mapOf("instanceId" to "ins-1", "sgmVersion" to "2", "actions" to actionsJson))

    @Test
    fun parsesTwoActionsWithDeeplinkFallback() {
        val payload = payloadWithActions(
            """[{"action":"view","title":"View","redirectUrl":"https://shop.example.com/view","deeplink":"app://view"},""" +
                """{"action":"shop","title":"Shop Now","redirectUrl":"https://shop.example.com/shop"}]"""
        )

        assertEquals(
            listOf(
                PushActionSpec("action0", "View", "app://view"),
                PushActionSpec("action1", "Shop Now", "https://shop.example.com/shop")
            ),
            PushActionSpec.from(payload)
        )
    }

    @Test
    fun showsAtMostTwoActions() {
        val payload = payloadWithActions(
            """[{"action":"a","title":"One","redirectUrl":"https://shop.example.com/1"},""" +
                """{"action":"b","title":"Two","redirectUrl":"https://shop.example.com/2"},""" +
                """{"action":"c","title":"Three","redirectUrl":"https://shop.example.com/3"}]"""
        )

        assertEquals(listOf("One", "Two"), PushActionSpec.from(payload).map { it.title })
    }

    @Test
    fun skipsAnActionWithoutTitleOrTargetAndNumbersTheShownButtons() {
        val payload = payloadWithActions(
            """[{"action":"a","title":"No link"},""" +
                """{"action":"b","redirectUrl":"https://shop.example.com/untitled"},""" +
                """{"action":"c","title":"Shop Now","redirectUrl":"https://shop.example.com/shop"}]"""
        )

        assertEquals(listOf(PushActionSpec("action0", "Shop Now", "https://shop.example.com/shop")), PushActionSpec.from(payload))
    }

    @Test
    fun aPushWithoutActionsHasNoButtons() {
        assertTrue(PushActionSpec.from(SegmentifyPushPayload.from(mapOf("instanceId" to "ins-1"))).isEmpty())
        assertTrue(PushActionSpec.from(payloadWithActions("not-json")).isEmpty())
    }
}
