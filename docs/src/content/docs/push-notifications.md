---
title: "Push Notifications"
description: "Register the device with Segmentify and report views and clicks of push campaigns."
---

Segmentify sends push campaigns through Firebase Cloud Messaging (FCM). Your app receives and displays the notifications; the SDK registers the device and reports what happens to each notification.

## Set up

Set the push data center **before** `config`:

```kotlin
SegmentifyManager.setPushConfig("https://gimli-eu.segmentify.com")   // your push data center URL
SegmentifyManager.config(this, "YOUR_API_KEY", "https://gandalf-eu.segmentify.com", "www.yourdomain.com")
```

Add Firebase Cloud Messaging to your app as described in the [Firebase documentation](https://firebase.google.com/docs/cloud-messaging/android/client). The SDK does not include Firebase; your app receives the messages and passes them on.

## Register the device token

Send the token to Segmentify whenever you receive or refresh it.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.NotificationModel
import com.segmentify.segmentifyandroidsdk.model.NotificationType

override fun onNewToken(token: String) {
    val model = NotificationModel()
    model.type = NotificationType.PERMISSION_INFO
    model.deviceToken = token
    SegmentifyManager.sendNotification(model)
}
```

Send the token again on app start as well, so a token issued before the SDK was configured is not lost.

Without a registered token the user cannot be targeted by push campaigns.

## Report views and clicks

Build a `SegmentifyPushPayload` from the incoming message and hand it to the SDK.

```kotlin
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload

class MyFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isEmpty()) return

        val payload = SegmentifyPushPayload.from(remoteMessage.data)
        if (payload.title.isNotEmpty() || payload.message.isNotEmpty()) {
            showNotification(payload)   // your own code: build and show the notification
        }
        SegmentifyManager.handlePushReceived(payload)
    }
}
```

When the user taps the notification, report the click from the activity that opens:

```kotlin
SegmentifyManager.handlePushClicked(payload)
```

Keep the values you need (at least `instanceId`) in the notification's intent extras so you can rebuild the payload there, for example with `SegmentifyPushPayload.from(mapOf("instanceId" to instanceId))`.

`handlePushReceived` reports a view and `handlePushClicked` reports a click. Both need the payload's `instanceId`; a message without one is not a Segmentify campaign and is ignored.

## The payload

| Property | Type | Description |
| --- | --- | --- |
| `title`, `message` | `String` | Text of the notification. Empty when not sent. |
| `image`, `icon` | `String?` | Image and icon URLs. |
| `deeplink`, `redirectUrl` | `String?` | Where a tap should lead. |
| `targetUrl` | `String?` | `deeplink` if present, otherwise `redirectUrl`. |
| `instanceId` | `String?` | ID of the push campaign. Needed to report views and clicks. |
| `tag` | `String?` | Notification tag. |
| `actions` | `List<SegmentifyPushAction>` | Action buttons, each with `action`, `title`, `icon`, `redirectUrl`, `deeplink`. |
| `isV2` | `Boolean` | Whether the message uses the current payload format. |

## Attribute what happens next

After a click, the SDK remembers the campaign so you can attribute the visit that follows:

```kotlin
val utm = SegmentifyManager.getTrackingParameters()
// utm.utm_source = "segmentify", utm.utm_medium = "push", utm.utm_campaign = the campaign's instanceId
```

Add these to the deep link or to your own analytics events if you track campaign sources.

## Checklist

- The push data center URL is set.
- The token is sent on first launch and on every refresh.
- Views are reported when a notification arrives, and clicks when it is tapped.
- The notification opens `targetUrl` when one is present.
