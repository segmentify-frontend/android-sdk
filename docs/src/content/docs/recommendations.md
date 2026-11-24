---
title: "Recommendations"
description: "Render the recommendation widgets the SDK returns, and report their impressions and clicks."
---

Event methods with a callback return the recommendation campaigns that apply to the event. Which campaigns, with which products, is set up in your Segmentify panel; the app only renders what comes back.

## What you receive

A list of recommendations, one per campaign. It is empty when no campaign applies.

| Property | Description |
| --- | --- |
| `notificationTitle` | The title of the widget, as set in the panel. |
| `products` | The products to show, in order. |
| `instanceId` | ID of the campaign. Use it when reporting interactions. |
| `actionId` | ID of this response. Use it as `interactionId` for impressions and widget views. |
| `errorString` | Set when the response could not be read. |

Each product has, among others:

| Property | Description |
| --- | --- |
| `productId` | Your ID of the product. |
| `name` | Product name. |
| `url`, `mUrl` | Product page URLs. |
| `image`, `imageXS` ... `imageXL` | Image URLs. |
| `price`, `oldPrice`, `specialPrice` | Prices as numbers. |
| `priceText`, `oldPriceText`, `specialPriceText` | Prices formatted for display. |
| `brand`, `category`, `categories` | Brand and categories. |
| `inStock`, `stockCount` | Availability. |
| `params` | Custom product fields from your catalogue. |

## Render and report

```kotlin
override fun onDataLoaded(data: ArrayList<RecommendationModel>) {
    for (recommendation in data) {
        val title = recommendation.notificationTitle
        val products = recommendation.products ?: continue
        showWidget(title, products)   // your own UI code
        SegmentifyManager.sendImpression(recommendation.instanceId!!, recommendation.actionId!!)
    }
}
```

The callback is delivered by the networking layer. Switch to the main thread before touching views if your UI code needs it.

1. Render each recommendation as a widget: its title and its products.
2. Call `sendImpression` when you place the widget.
3. Call `sendWidgetView` when it becomes visible.
4. Call `sendClickView` with the product ID when a product is tapped.

See [Interactions and Banners](/events/interactions/) for the three calls.

## Where recommendations come from

| Screen | Call that returns them |
| --- | --- |
| Home, category and other screens | `sendPageView` |
| Product | `sendProductView` |
| Basket | `sendViewBasket` |
| Order confirmation | `sendPurchase` |
| Anything else | `sendCustomEvent` |

If you get an empty list, check in the panel that a campaign is active for that page type and that test mode matches: a campaign in test mode only answers events sent with `testMode` on.
