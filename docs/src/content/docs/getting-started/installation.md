---
title: "Installation"
description: "Add the Segmentify Android SDK to your project."
---

The Segmentify Android SDK supports **Android 7.0 (API level 24)** and higher.

## Add the SDK

Add Maven Central to your project-level `build.gradle`, if it is not there already:

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
}
```

Then add the SDK to your app module's `build.gradle`. Replace `x.y.z` with the latest version on [Maven Central](https://mvnrepository.com/artifact/com.segmentify.sdk/android):

```groovy
dependencies {
    implementation 'com.segmentify.sdk:android:x.y.z'
}
```

The SDK needs the `INTERNET` and `ACCESS_NETWORK_STATE` permissions. Its own manifest declares them, so the manifest merger adds them to your app; you do not need to add them yourself.

## What you need from Segmentify

Have these ready before you configure the SDK. The API key and data center are under **Settings > Integrations** in the Segmentify panel.

| You need | Example | Used for |
| --- | --- | --- |
| API key | `xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx` | Identifies your account. |
| Data center URL | `https://gandalf-eu.segmentify.com` | The host your account lives on. |
| Domain | `www.yourdomain.com` | The domain registered for your account. The SDK sends it with every request. |
| Push data center URL | `https://gimli-eu.segmentify.com` | Only if you use push notifications. |

Data centers by region:

| Region | Data center URL | Push data center URL |
| --- | --- | --- |
| Türkiye | `https://per2.segmentify.com` | `https://psh2.segmentify.com` |
| Europe | `https://gandalf-eu.segmentify.com` | `https://gimli-eu.segmentify.com` |
| Latin America | `https://gandalf-br.segmentify.com` | `https://gimli-br.segmentify.com` |

Use the one shown in your panel. An API key sent to another data center is not found.

Next: [Configuration](/getting-started/configuration/).
