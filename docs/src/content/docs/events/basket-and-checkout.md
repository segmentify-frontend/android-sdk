---
title: "Basket and Checkout"
description: "Report basket changes and the steps of the checkout funnel, from viewing the basket to a completed order."
---

## Basket changes

Call `sendAddOrRemoveBasket` when a product is added to or removed from the basket.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.BasketModel

val basket = BasketModel()
basket.step = "add"            // or "remove"
basket.productId = "SKU-1001"
basket.quantity = 1.0
basket.price = 135.0
basket.lang = "EN"
basket.currency = "EUR"
SegmentifyManager.sendAddOrRemoveBasket(basket)
```

A shorter form takes the values directly; here all four are required:

```kotlin
SegmentifyManager.sendAddOrRemoveBasket("add", "SKU-1001", 1.0, 135.0)
```

| Property | Required | Description |
| --- | --- | --- |
| `step` | Yes | `add` or `remove`. |
| `productId` | Yes | The product. |
| `quantity` | Yes | How many were added or removed. |
| `price` | Recommended | Unit price. |

Without `step`, `productId` or `quantity` the event is not sent.

## Checkout steps

The checkout methods all take the basket's total and its products.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.CheckoutModel

val item = ProductModel()
item.productId = "SKU-1001"
item.price = 135.0
item.quantity = 1.0

val checkout = CheckoutModel()
checkout.totalPrice = 135.0
checkout.productList = arrayListOf(item)
checkout.lang = "EN"
checkout.currency = "EUR"
```

Pass a `CheckoutModel` to these methods, as shown. `sendViewBasket` and `sendPurchase` also have shorter overloads that take `totalPrice` and `productList` directly.

Then call the method for the step the user is on:

```kotlin
SegmentifyManager.sendViewBasket(checkout, callback)            // basket screen
SegmentifyManager.sendCustomerInformation(checkout, callback)   // address / customer step
SegmentifyManager.sendPaymentInformation(checkout, callback)    // payment step

checkout.orderNo = "ORDER-98765"
SegmentifyManager.sendPurchase(checkout, callback)              // order completed
```

| Method | When |
| --- | --- |
| `sendViewBasket` | The basket screen opens. |
| `sendCustomerInformation` | The user is on the customer or address step. |
| `sendPaymentInformation` | The user is on the payment step. |
| `sendPurchase` | The order is completed. |

| Property | Required | Description |
| --- | --- | --- |
| `totalPrice` | Yes | Total of the basket or order. |
| `productList` | Yes | The products, each with `productId`, `quantity` and `price`. |
| `orderNo` | For purchase | Your order number. |

## Rules

- Call `sendPurchase` **once per order**. Calling it again on a screen refresh counts the revenue twice.
- `totalPrice` and the product prices are in the currency named by `currency`.
- All four methods return recommendations to their callback, for example "you may also like" on the basket screen.
