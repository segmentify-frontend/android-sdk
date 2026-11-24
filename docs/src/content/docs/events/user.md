---
title: "User Events"
description: "Tell Segmentify who the user is: sign-up, sign-in, profile changes, consent and channel subscriptions."
---

There are two sets of user methods. Use the **CDP user events** in new integrations. The older **user operations** are still supported.

## CDP user events

Each method takes the user's profile.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.CdpEventsPayload
import com.segmentify.segmentifyandroidsdk.model.Consent

val profile = CdpEventsPayload(
    email = "jane@example.com",
    phone = "905551234567",
    username = "jane",
    fullName = "Jane Doe",
    gender = "female",
    emailConsent = Consent.SUBSCRIBED,
    custom = mapOf("loyaltyLevel" to "gold")
)

SegmentifyManager.registerUser(profile)   // after sign-up
SegmentifyManager.loginUser(profile)      // after sign-in
SegmentifyManager.identityUser(profile)   // when the profile is known or has changed
SegmentifyManager.userTraits(profile)     // to update traits
SegmentifyManager.logoutUser(profile)     // after sign-out
```

| Property | Type | Description |
| --- | --- | --- |
| `email`, `phone`, `username` | `String?` | Identifiers of the user. |
| `fullName`, `gender` | `String?` | Profile fields. |
| `source` | `String?` | Where the data came from. |
| `emailConsent`, `smsConsent`, `whatsappConsent`, `callConsent` | `Consent?` | `SUBSCRIBED`, `UNSUBSCRIBED` or `NOT_SET`. |
| `custom` | `Map<String, Any?>?` | Your own attributes. |

| Method | When |
| --- | --- |
| `registerUser` | The user completed sign-up. |
| `loginUser` | The user signed in. |
| `identityUser` | The app knows who the user is, or the profile changed. Sent only when the profile differs from the last one sent, so it is safe to call often. |
| `userTraits` | To update profile traits. |
| `logoutUser` | The user signed out. |

Behaviour to know:

- An empty profile is not sent.
- If you set at least one consent field, the others are sent as `NOT_SET`. If you set none, no consent is sent and stored consents are left as they are.

## Channel subscriptions

Report when a user subscribes to, or unsubscribes from, a channel.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.Channel
import com.segmentify.segmentifyandroidsdk.model.Purpose
import com.segmentify.segmentifyandroidsdk.model.SubscribeProperties
import com.segmentify.segmentifyandroidsdk.model.UnsubscribePayload

SegmentifyManager.subscribeUser(SubscribeProperties.Email("jane@example.com", listOf(Purpose.MARKETING)))
SegmentifyManager.subscribeUser(SubscribeProperties.Sms("905551234567", listOf(Purpose.MARKETING)))
SegmentifyManager.subscribeUser(SubscribeProperties.Whatsapp("905551234567", listOf(Purpose.MARKETING)))
SegmentifyManager.subscribeUser(SubscribeProperties.Call("905551234567", listOf(Purpose.MARKETING)))
SegmentifyManager.subscribeUser(SubscribeProperties.AppPush(fcmToken))

SegmentifyManager.unsubscribeUser(UnsubscribePayload(channel = Channel.EMAIL, email = "jane@example.com"))
SegmentifyManager.unsubscribeUser(UnsubscribePayload(channel = Channel.SMS, phone = "905551234567"))
```

`Purpose` is `MARKETING`, `TRANSACTIONAL`, `PERSONALISATION` or `ALL`.

## User operations (legacy)

```kotlin
import com.segmentify.segmentifyandroidsdk.model.UserModel

val user = UserModel()
user.email = "jane@example.com"
user.externalId = "jane"          // your username or user ID
user.fullName = "Jane Doe"

SegmentifyManager.sendUserRegister(user)                       // sign-up
SegmentifyManager.sendUserLogin(user)                          // sign-in
SegmentifyManager.sendUserUpdate(user)                         // profile change
SegmentifyManager.sendUserLogout("jane", "jane@example.com")   // sign-out
```

`sendUserRegister`, `sendUserLogin` and `sendUserUpdate` need `email`; without it they log an error and send nothing.

Do not send both sets for the same action. Pick one per integration.
