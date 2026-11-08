package com.segmentify.segmentifysdk;

import android.app.Application;

import com.segmentify.segmentifyandroidsdk.SegmentifyManager;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // if you are using push module make sure to call SegmentifyManager.Instance.setPushConfig first
        // Defaults are the original Segmentify demo values. To test another environment,
        // override segmentify.* keys in local.properties (see app/build.gradle).
        SegmentifyManager.INSTANCE.setPushConfig(BuildConfig.PUSH_API_URL);
        SegmentifyManager.INSTANCE.config(this, BuildConfig.API_KEY, BuildConfig.DATA_CENTER_URL, BuildConfig.SUBDOMAIN, BuildConfig.AUTH_HEADER);
        SegmentifyManager.INSTANCE.setSessionKeepSecond(86400);
        SegmentifyManager.INSTANCE.logStatus(true);
    }
}
