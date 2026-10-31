package com.segmentify.segmentifysdk;

import android.app.Application;

import com.segmentify.segmentifyandroidsdk.SegmentifyManager;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // if you are using push module make sure to call SegmentifyManager.Instance.setPushConfig first
        SegmentifyManager.INSTANCE.setPushConfig(BuildConfig.PUSH_API_URL);
        // Values come from local.properties (segmentify.pushApiUrl, segmentify.subDomain, segmentify.authHeader)
        SegmentifyManager.INSTANCE.config(this, null, BuildConfig.PUSH_API_URL, BuildConfig.SUBDOMAIN, BuildConfig.AUTH_HEADER);
        SegmentifyManager.INSTANCE.setUserId("-896626520993357823");
        SegmentifyManager.INSTANCE.setSessionKeepSecond(86400);
        SegmentifyManager.INSTANCE.logStatus(true);
    }
}
