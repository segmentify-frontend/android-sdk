# Segmentify Android SDK
Segmentify  SDK for sending events and rendering recommendations for android based devices

> **Supports Android 4.4(API Level: 24) and higher devices.**
> **Current version : 2.3.0*

## Installation

You can install Segmentify Android SDK to your application by using  [Maven](https://mvnrepository.com/artifact/com.segmentify.sdk/android).

To use Maven, add the project gradle file:


```java
buildscript {
    repositories {
        mavenCentral()
    }
}
```

Please add following line to your gradle file:

Gradle : 
```java
implementation 'com.segmentify.sdk:android:*.*.*'
```
or If you are using another tool, you can add it as follows :

Maven:
```xml
<!-- https://mvnrepository.com/artifact/com.segmentify.sdk/android -->
<dependency>
    <groupId>com.segmentify.sdk</groupId>
    <artifactId>android</artifactId>
    <version>***</version>
</dependency>
```
SBT :
```scala
// https://mvnrepository.com/artifact/com.segmentify.sdk/android
libraryDependencies += "com.segmentify.sdk" % "android" % "*.*.*"
```

Ivy :
```xml
<!-- https://mvnrepository.com/artifact/com.segmentify.sdk/android -->
<dependency org="com.segmentify.sdk" name="android" rev="*.*.*"/>
```

Grape : 
```xml
<!-- https://mvnrepository.com/artifact/com.segmentify.sdk/android -->
<dependency org="com.segmentify.sdk" name="android" rev="*.*.*"/>
```

Leiningen : 
```Clojure 
;; https://mvnrepository.com/artifact/com.segmentify.sdk/android
[com.segmentify.sdk/android "*.*.*"]
```

Buildr : 
```
# https://mvnrepository.com/artifact/com.segmentify.sdk/android
'com.segmentify.sdk:android:jar:*.*.*'

```


## Usage

To learn more about how to integrate Segmentify Android SDK to your application, please check [Integration Guide](https://www.segmentify.com/dev/integration_android/).

For other integrations you can check [Master Integration](https://www.segmentify.com/dev/) guide too.



## License

Segmentify Android SDK is available under the BSD-2 license.
Please check LICENSE file to learn more about details.


## Push Permission & User Updates 2.2.6
2.2.6 update changes user events in the background. This is a completely internal and will not effect sdk users. Also productId parameter has been removed from push interaction event.

## Wishlist Events 2.3.0
2.3.0 adds the `WISHLIST` event, so app users' favourites reach the wishlist counters and the "Favorite Activity" social proof. Supported steps are `add`, `remove`, `detail` and `view-favorites`.

`add`, `remove` and `detail` need a non-blank `productId`. `view-favorites` needs a `favoritesList` (an empty list is allowed). Calls with a missing field or an unknown step are not sent, and the SDK logs an error.

Kotlin:
```kotlin
SegmentifyManager.sendWishlistAdd("product-1")
SegmentifyManager.sendWishlistRemove("product-1")
SegmentifyManager.sendWishlistDetail("product-1")
SegmentifyManager.sendWishlistViewFavorites(listOf("product-1", "product-2"))

// or with the model
val wishlistModel = WishlistModel()
wishlistModel.step = "add"
wishlistModel.productId = "product-1"
SegmentifyManager.sendWishlist(wishlistModel)
```

Java:
```java
SegmentifyManager.INSTANCE.sendWishlistAdd("product-1");
SegmentifyManager.INSTANCE.sendWishlistRemove("product-1");
SegmentifyManager.INSTANCE.sendWishlistDetail("product-1");
SegmentifyManager.INSTANCE.sendWishlistViewFavorites(Arrays.asList("product-1", "product-2"));

// or with the model
WishlistModel wishlistModel = new WishlistModel();
wishlistModel.setStep("add");
wishlistModel.setProductId("product-1");
SegmentifyManager.INSTANCE.sendWishlist(wishlistModel);
```

Good Luck!



