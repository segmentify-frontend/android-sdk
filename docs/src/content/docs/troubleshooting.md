---
title: "Troubleshooting"
description: "What to check when an event is not sent, recommendations are empty or push notifications are not reported."
---

## Turn on logs

Turn on logs with `SegmentifyManager.logStatus(true)` (after `config`) and filter Logcat by `Segmentify`. The SDK logs an error whenever it refuses to send an event, naming the missing field.

## The event is not sent

The SDK checks required fields before sending. If one is missing it logs an error and **does not send the event**; no exception is thrown.

| Method | Not sent when |
| --- | --- |
| `sendPageView` | `category` is missing. |
| `sendProductView` | `productId`, `title`, `url`, `image` or `price` is missing; or both or neither of `category` and `categories` are set. |
| `sendAddOrRemoveBasket` | `step`, `productId` or `quantity` is missing. |
| `sendViewBasket`, `sendPurchase` and the other checkout methods | `totalPrice` or `productList` is missing. |
| `sendWishlist` | The step is unknown, or its `productId` or `favoritesList` is missing. |
| Push methods | The push data center URL is not set, or the token or `instanceId` is missing. |

## The event is sent but nothing happens

| Symptom | Likely cause |
| --- | --- |
| Nothing arrives in the panel | Wrong API key or data center URL, or the domain passed to the SDK is not the one registered for the account. |
| Events arrive, recommendations are empty | No active campaign for that page type, or the campaign is in test mode and the event is not (or the other way round). |
| No result for some users | `lang` or `currency` is missing or not configured for the account. |
| Product recommendations show wrong prices | `sendProductView` sends a different price or currency than your product feed. |
| Revenue is counted twice | `sendPurchase` is called more than once for an order. |
| Every launch looks like a new visitor | App data is cleared between launches, so the stored user ID is lost. |

## Push notifications

| Symptom | Likely cause |
| --- | --- |
| Users are not reachable | The token was never sent: check the registration call runs after the SDK is configured. |
| Sends are reported, views and clicks are not | `handlePushReceived` and `handlePushClicked` are not called, or the payload has no `instanceId`. |
| Nothing is reported at all | The push data center URL is not set or belongs to another region. |

## Check the request itself

The SDK calls the Segmentify Events API. To see exactly what a correct request and response look like, and to send one by hand, use the [API Docs](https://api-docs.segmentify.com/): the [status codes](https://api-docs.segmentify.com/events/responses-and-errors/) explain every rejection.

## Still stuck

Send the Segmentify support team the SDK version, the log lines around the event, and the time of the test.
