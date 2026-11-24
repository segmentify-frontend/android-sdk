---
title: "Custom Event"
description: "Send an event of your own to trigger Segmentify campaigns."
---

Use a custom event for anything the standard events do not cover, such as a newsletter sign-up or a store-locator search. Campaigns in the panel can be triggered by its `type`.

```kotlin
import com.segmentify.segmentifyandroidsdk.model.CustomEventModel

val event = CustomEventModel()
event.type = "newsletter-signup"
event.lang = "EN"
SegmentifyManager.sendCustomEvent(event, callback)

// or, without extra fields
SegmentifyManager.sendCustomEvent("newsletter-signup", callback)
```

| Property | Required | Description |
| --- | --- | --- |
| `type` | Yes | Your name for the event. Must match the name used in the panel. |
| `params` | No | Extra values to send with the event. |

The callback receives recommendations if a campaign answers the event.
