package com.segmentify.segmentifyandroidsdk

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.core.app.NotificationManagerCompat
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload
import com.segmentify.segmentifyandroidsdk.utils.SegmentifyLogger

class SegmentifyPushActionActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleClick(intent)
        finish()
    }

    private fun handleClick(clickIntent: Intent?) {
        if (clickIntent == null) return
        NotificationManagerCompat.from(this).cancel(clickIntent.getIntExtra(EXTRA_NOTIFICATION_ID, 0))
        reportClick(clickIntent)
        open(clickIntent.getStringExtra(EXTRA_TARGET))
    }

    private fun reportClick(clickIntent: Intent) {
        try {
            val payload = SegmentifyPushPayload.from(pushData(clickIntent))
            val actionId = clickIntent.getStringExtra(EXTRA_ACTION_ID)
            if (actionId == null) {
                SegmentifyManager.handlePushClicked(payload)
            } else {
                SegmentifyManager.handlePushActionClicked(payload, actionId)
            }
        } catch (e: Exception) {
            SegmentifyLogger.printErrorLog("Error occurred when sending push click, error: $e")
        }
    }

    @Suppress("DEPRECATION")
    private fun pushData(clickIntent: Intent): Map<String, String?> {
        val data = clickIntent.getSerializableExtra(EXTRA_DATA) as? Map<*, *> ?: return emptyMap()
        return data.entries.associate { it.key.toString() to it.value?.toString() }
    }

    private fun open(target: String?) {
        val targetIntent = targetIntent(target) ?: return
        targetIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        try {
            startActivity(targetIntent)
        } catch (e: ActivityNotFoundException) {
            SegmentifyLogger.printErrorLog("No activity can open the push target, error: $e")
        }
    }

    private fun targetIntent(target: String?): Intent? {
        if (target.isNullOrBlank()) {
            return packageManager.getLaunchIntentForPackage(packageName)
        }
        return Intent(Intent.ACTION_VIEW, Uri.parse(target))
    }

    companion object {
        internal const val EXTRA_DATA = "com.segmentify.segmentifyandroidsdk.push.DATA"
        internal const val EXTRA_NOTIFICATION_ID = "com.segmentify.segmentifyandroidsdk.push.NOTIFICATION_ID"
        internal const val EXTRA_ACTION_ID = "com.segmentify.segmentifyandroidsdk.push.ACTION_ID"
        internal const val EXTRA_TARGET = "com.segmentify.segmentifyandroidsdk.push.TARGET"
    }
}
