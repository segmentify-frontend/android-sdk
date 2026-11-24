---
title: "Configuration"
description: "Initialise the Segmentify Android SDK, and set session length, logging and the fields every event shares."
---

## Initialise the SDK

Configure the SDK once, when the app starts, before sending any event.

```kotlin
import android.app.Application
import com.segmentify.segmentifyandroidsdk.SegmentifyManager

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SegmentifyManager.config(
            this,
            "YOUR_API_KEY",
            "https://gandalf-eu.segmentify.com",   // your data center URL
            "www.yourdomain.com"                    // the domain registered for your account
        )
    }
}
```

Register the class in `AndroidManifest.xml` with `android:name=".MyApp"` on the `<application>` element.

From Java, `SegmentifyManager` is a Kotlin object: call it as `SegmentifyManager.INSTANCE.config(...)`.

| Parameter | Description |
| --- | --- |
| API key | Your Segmentify API key. |
| Data center URL | The full URL of your data center, starting with `https://`. |
| Domain | The domain registered for your Segmentify account. It is sent as the `Origin` of every request and must match the account, or requests are rejected. |

- `config` does nothing and logs an error if any of the three values is blank.
- Call `setSessionKeepSecond` and `logStatus` **after** `config`. Before it, they have no effect.

## Options

```kotlin
SegmentifyManager.setSessionKeepSecond(86400)   // session length in seconds; 86400 is the default
SegmentifyManager.logStatus(true)                // print SDK logs to Logcat
SegmentifyManager.setAppVersion("3.2.1")         // optional: your app version, sent with events
```

| Option | Default | Description |
| --- | --- | --- |
| Session length | `86400` seconds (24 hours) | How long a session ID is kept before a new one is created. |
| Logging | off | Prints what the SDK does, including why an event was not sent. |

## User and session IDs

You do not create user or session IDs. On first launch the SDK asks Segmentify for a user ID and a session ID and stores them in shared preferences. The user ID is kept; the session ID is replaced when the session length has passed.

## Fields every event shares

Every event model has these properties. Set them on each event you send.

```kotlin
val page = PageModel()
page.category = "Home Page"
page.lang = "EN"
page.currency = "EUR"
page.testMode = true     // remove before going live
```

| Property | Description |
| --- | --- |
| `lang` | Language of the content, for example `EN`. Must be a language configured for your account. Set it on every event: when it is missing, `EN` is assumed, and accounts without English get no result. |
| `currency` | Currency of the prices, for example `EUR`. Must be a currency configured for your account. |
| `region` | Region of the user, if your account uses regions. |
| `params` | Custom key-value pairs to send with the event. |
| `testMode` | `true` marks a test event. Test events are answered by test-mode campaigns and kept apart from live data. |
| `pageUrl` | URL or deep link of the screen. |

## Switching account at runtime

If one app serves several Segmentify accounts (for example one per country), change the configuration when the user switches:

```kotlin
SegmentifyManager.setConfig("OTHER_API_KEY", "https://gandalf-eu.segmentify.com", "www.otherdomain.com")
```

Next: [Your first integration](/getting-started/first-integration/).
