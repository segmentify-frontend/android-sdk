---
title: "Interactions and Banners"
description: "Report when a recommendation widget is shown, seen and clicked, and track banners."
---

Segmentify measures a campaign by its interactions. Without them, widgets show up in your app but their reports stay empty.

## Widget interactions

```kotlin
// the widget was returned and placed on the screen
SegmentifyManager.sendImpression(recommendation.instanceId!!, recommendation.actionId!!)

// the widget became visible to the user
SegmentifyManager.sendWidgetView(recommendation.instanceId!!, recommendation.actionId!!)

// the user tapped a product of the widget
SegmentifyManager.sendClickView(recommendation.instanceId!!, product.productId!!)
```

| Method | When | `interactionId` |
| --- | --- | --- |
| `sendImpression` | The widget was returned and placed on the screen. | The `actionId` of the recommendation. |
| `sendWidgetView` | The widget scrolled into view. | The `actionId` of the recommendation. |
| `sendClickView` | The user tapped a product in the widget. | The product ID. |
| `sendSearchClickView` | The user tapped a product in search results. | The product ID. |

`instanceId` is always the ID of the campaign, taken from the recommendation or search response.

Send an impression once per widget per screen load, not on every scroll.

## Banners

If you use Segmentify's banner tracking, report the banners your app shows and the taps on them.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.BannerGroupViewModel
import com.segmentify.segmentifyandroidsdk.model.BannerOperationsModel
import com.segmentify.segmentifyandroidsdk.model.InternalBannerModel

val banner = BannerOperationsModel()
banner.group = "Home Page Slider"
banner.title = "Summer Sale"
banner.order = 1

SegmentifyManager.sendBannerImpressionEvent(banner)
SegmentifyManager.sendBannerClickEvent(banner)

val slide = InternalBannerModel()
slide.title = "Summer Sale"
slide.image = "https://cdn.yourdomain.com/banners/summer.jpg"
slide.order = 1
slide.urls = arrayListOf("https://www.yourdomain.com/summer")

val group = BannerGroupViewModel()
group.group = "Home Page Slider"
group.banners = arrayListOf(slide)
SegmentifyManager.sendBannerGroupViewEvent(group)
```

| Method | When |
| --- | --- |
| `sendBannerGroupViewEvent` | A banner group (for example a slider) is shown, with the banners in it. |
| `sendBannerImpressionEvent` | One banner became visible. |
| `sendBannerClickEvent` | The user tapped a banner. |
| `sendBannerUpdateEvent` | A banner's content changed. |

`group` is the name of the banner area and must be the same in every event for that area.
