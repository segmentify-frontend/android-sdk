package com.segmentify.segmentifyandroidsdk.utils

object WishlistValidation {

    private val productSteps = listOf(Constant.wishlistAddStep, Constant.wishlistRemoveStep, Constant.wishlistDetailStep)

    fun findError(step: String?, productId: String?, favoritesList: List<String>?): String? {
        if (step == Constant.wishlistViewFavoritesStep) {
            if (favoritesList == null) return "You must fill favoritesList before accessing sendWishlist event method with step $step"
            return null
        }
        if (step !in productSteps) return "You must fill step with add, remove, detail or view-favorites before accessing sendWishlist event method"
        if (productId.isNullOrBlank()) return "You must fill productId before accessing sendWishlist event method with step $step"
        return null
    }
}
