package com.segmentify.segmentifyandroidsdk

import com.segmentify.segmentifyandroidsdk.utils.Constant
import com.segmentify.segmentifyandroidsdk.utils.PushInteraction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
}
