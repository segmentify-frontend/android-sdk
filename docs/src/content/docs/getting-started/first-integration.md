---
title: "Your first integration"
description: "A screen-by-screen plan for a first Segmentify Android SDK integration, with a checklist for going live."
---

This page is the order to build things in. Each step links to the page with the code.

## How it works

Your app tells Segmentify what the user does by calling one SDK method per action. Segmentify answers some of those calls with **recommendations**: lists of products to show on that screen, as set up in your Segmentify panel. So the same calls both collect data and fetch content.

Methods that can return recommendations take `SegmentifyCallback<T>`, an interface with a single `onDataLoaded(data: T)` method. Methods that only record something (basket changes, clicks, user events) return nothing.

## Step 1: Install and configure

1. [Install the SDK](/getting-started/installation/).
2. [Configure it](/getting-started/configuration/) at app start with your API key, data center URL and domain.
3. Turn on logs while you develop.

## Step 2: Send a page view from every screen

Call `sendPageView` when a screen opens, with the screen's `category`. This is the minimum integration: it identifies visitors and lets campaigns target screens.

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

See [Page View](/events/page-view/) for the category names.

## Step 3: Add the shopping events

| When | Call | Page |
| --- | --- | --- |
| A product screen opens | `sendProductView` (and `sendPageView` with `Product Page`) | [Product View](/events/product-view/) |
| A product is added to or removed from the basket | `sendAddOrRemoveBasket` | [Basket and Checkout](/events/basket-and-checkout/) |
| The basket screen opens | `sendViewBasket` | [Basket and Checkout](/events/basket-and-checkout/) |
| An order is completed | `sendPurchase` | [Basket and Checkout](/events/basket-and-checkout/) |

These four are what recommendations and revenue reports are built on. Get them right before adding anything else.

## Step 4: Show recommendations

Render the recommendations returned to your callbacks, and report when a widget is shown and when a product in it is tapped. See [Recommendations](/recommendations/).

## Step 5: Add what your app needs

| Feature | Page |
| --- | --- |
| Sign-up, sign-in, profile and consent | [User Events](/events/user/) |
| Wishlist | [Wishlist](/events/wishlist/) |
| Search | [Search](/events/search/) |
| Banners | [Interactions and Banners](/events/interactions/) |
| Your own triggers | [Custom Event](/events/custom-event/) |
| Push notifications | [Push Notifications](/push-notifications/) |

## Go-live checklist

- `testMode` is removed, or `false`, on every event.
- `lang` and `currency` are set on every event and match your account.
- `sendProductView` sends the real price, stock status, product URL and image.
- `sendPurchase` is called once per order, with `orderNo`.
- Product IDs are the same ones as in your product catalogue and on your website.
- Logging is off in release builds.

If an event does not arrive, see [Troubleshooting](/troubleshooting/).
