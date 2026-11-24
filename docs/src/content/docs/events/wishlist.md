---
title: "Wishlist"
description: "Report wishlist additions, removals and views."
---

```kotlin
SegmentifyManager.sendWishlistAdd("SKU-1001")
SegmentifyManager.sendWishlistRemove("SKU-1001")
SegmentifyManager.sendWishlistDetail("SKU-1001")
SegmentifyManager.sendWishlistViewFavorites(listOf("SKU-1001", "SKU-2002"))
```

To set `lang`, `currency` or `params`, build a `WishlistModel` and call `sendWishlist`:

```kotlin
import com.segmentify.segmentifyandroidsdk.model.WishlistModel

val wishlist = WishlistModel()
wishlist.step = "add"          // add, remove, detail or view-favorites
wishlist.productId = "SKU-1001"
wishlist.lang = "EN"
SegmentifyManager.sendWishlist(wishlist)
```

| Step | Method | Needs |
| --- | --- | --- |
| `add` | `sendWishlistAdd` | `productId` |
| `remove` | `sendWishlistRemove` | `productId` |
| `detail` | `sendWishlistDetail` | `productId`. The user opened a product from the wishlist. |
| `view-favorites` | `sendWishlistViewFavorites` | `favoritesList`, the product IDs in the wishlist. May be empty. |

The event is not sent when the step is not one of the four, or when the field it needs is missing.
