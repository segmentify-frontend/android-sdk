---
title: "Page View"
description: "Report every screen the user opens with sendPageView, and receive the recommendations for that screen."
---

Send a page view each time a screen opens. The `category` says what kind of screen it is.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.PageModel
import com.segmentify.segmentifyandroidsdk.model.RecommendationModel
import com.segmentify.segmentifyandroidsdk.utils.SegmentifyCallback

val page = PageModel()
page.category = "Home Page"
page.lang = "EN"
page.currency = "EUR"

SegmentifyManager.sendPageView(page, object : SegmentifyCallback<ArrayList<RecommendationModel>> {
    override fun onDataLoaded(data: ArrayList<RecommendationModel>) {
        // data holds the recommendation widgets to show on this screen
    }
})
```

## Categories

Use these names for the standard screens. They are what campaigns in the panel target.

| `category` | Screen |
| --- | --- |
| `Home Page` | Home screen |
| `Category Page` | A product listing. Also set `subCategory`. |
| `Product Page` | A product's detail screen. Also send [Product View](/events/product-view/). |
| `Search Page` | Search results |
| `Basket Page` | Basket |
| `Checkout Success Page` | Order confirmation |
| `404 Page` | Content not found |

For any other screen, use your own name, for example `Account Page`.

## Category screens

```kotlin
val page = PageModel()
page.category = "Category Page"
page.subCategory = "Shoes > Sneakers"
page.lang = "EN"
page.currency = "EUR"
SegmentifyManager.sendPageView(page, callback)
```

## Properties

| Property | Required | Description |
| --- | --- | --- |
| `category` | Yes | The screen type. Without it the event is not sent. |
| `subCategory` | No | The category shown on a category screen. |
Plus the [shared fields](/getting-started/configuration/#fields-every-event-shares).

## The result

The callback receives a list of recommendation widgets for the screen. It is empty when no campaign targets it. See [Recommendations](/recommendations/).
