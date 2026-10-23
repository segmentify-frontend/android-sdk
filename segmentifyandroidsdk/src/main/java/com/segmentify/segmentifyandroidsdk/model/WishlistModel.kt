package com.segmentify.segmentifyandroidsdk.model

import com.google.gson.annotations.SerializedName
import java.util.*

class WishlistModel : SegmentifyObject() {
    @SerializedName("step")
    var step:String? = null

    var productId:String? = null
    var favoritesList:List<String>? = null
    var activeBanners:LinkedList<ClickedBannerObject> = LinkedList()
}
