package com.segmentify.segmentifyandroidsdk

import com.segmentify.segmentifyandroidsdk.utils.Constant
import com.segmentify.segmentifyandroidsdk.utils.WishlistValidation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class WishlistValidationTest {

    @Test
    fun wishlistEventNameAndStepsMatchWebSdk() {
        assertEquals("WISHLIST", Constant.wishlistEventName)
        assertEquals("add", Constant.wishlistAddStep)
        assertEquals("remove", Constant.wishlistRemoveStep)
        assertEquals("detail", Constant.wishlistDetailStep)
        assertEquals("view-favorites", Constant.wishlistViewFavoritesStep)
    }

    @Test
    fun addWithProductIdIsAccepted() {
        assertNull(WishlistValidation.findError(Constant.wishlistAddStep, "p1", null))
    }

    @Test
    fun addWithoutProductIdIsRejected() {
        assertNotNull(WishlistValidation.findError(Constant.wishlistAddStep, null, null))
    }

    @Test
    fun removeWithBlankProductIdIsRejected() {
        assertNotNull(WishlistValidation.findError(Constant.wishlistRemoveStep, " ", null))
    }

    @Test
    fun detailWithoutProductIdIsRejected() {
        assertNotNull(WishlistValidation.findError(Constant.wishlistDetailStep, null, listOf("p1")))
    }

    @Test
    fun viewFavoritesWithFavoritesListIsAccepted() {
        assertNull(WishlistValidation.findError(Constant.wishlistViewFavoritesStep, null, listOf("p1", "p2")))
    }

    @Test
    fun viewFavoritesWithoutFavoritesListIsRejected() {
        assertNotNull(WishlistValidation.findError(Constant.wishlistViewFavoritesStep, "p1", null))
    }

    @Test
    fun unknownStepIsRejected() {
        assertNotNull(WishlistValidation.findError("favorite", "p1", null))
    }

    @Test
    fun missingStepIsRejected() {
        assertNotNull(WishlistValidation.findError(null, "p1", null))
    }
}
