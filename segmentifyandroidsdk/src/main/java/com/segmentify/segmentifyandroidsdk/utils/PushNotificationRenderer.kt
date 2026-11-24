package com.segmentify.segmentifyandroidsdk.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.segmentify.segmentifyandroidsdk.SegmentifyPushActionActivity
import com.segmentify.segmentifyandroidsdk.model.SegmentifyPushPayload
import java.net.HttpURLConnection
import java.net.URL

internal object PushNotificationRenderer {

    private const val IMAGE_TIMEOUT_MILLIS = 5000
    private const val PENDING_INTENTS_PER_NOTIFICATION = PushActionSpec.MAX_ACTIONS + 1

    fun show(context: Context, data: Map<String, String>, channelId: String, smallIcon: Int): Boolean {
        val payload = SegmentifyPushPayload.from(data)
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) {
            SegmentifyLogger.printErrorLog("Push notification is not shown: notifications are turned off for the app")
            return false
        }
        createChannelIfMissing(context, manager, channelId)

        val notificationId = notificationId(payload)
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(smallIcon)
            .setContentTitle(payload.title)
            .setContentText(payload.message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(payload.message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(clickIntent(context, data, notificationId, 0, null, payload.targetUrl))
        loadImage(payload.image)?.let { image ->
            builder.setLargeIcon(image)
            builder.setStyle(NotificationCompat.BigPictureStyle().bigPicture(image).setSummaryText(payload.message))
        }
        PushActionSpec.from(payload).forEachIndexed { index, action ->
            builder.addAction(0, action.title, clickIntent(context, data, notificationId, index + 1, action.actionId, action.target))
        }

        return try {
            manager.notify(notificationId, builder.build())
            true
        } catch (e: SecurityException) {
            SegmentifyLogger.printErrorLog("Push notification is not shown: the notification permission is missing, error: $e")
            false
        }
    }

    private fun createChannelIfMissing(context: Context, manager: NotificationManagerCompat, channelId: String) {
        if (manager.getNotificationChannelCompat(channelId) != null) return
        val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
        manager.createNotificationChannel(
            NotificationChannelCompat.Builder(channelId, NotificationManagerCompat.IMPORTANCE_HIGH)
                .setName(appName)
                .build()
        )
    }

    private fun notificationId(payload: SegmentifyPushPayload): Int =
        (payload.tag ?: payload.instanceId ?: System.currentTimeMillis().toString()).hashCode()

    private fun clickIntent(context: Context, data: Map<String, String>, notificationId: Int, slot: Int, actionId: String?, target: String?): PendingIntent {
        val intent = Intent(context, SegmentifyPushActionActivity::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            .putExtra(SegmentifyPushActionActivity.EXTRA_DATA, HashMap(data))
            .putExtra(SegmentifyPushActionActivity.EXTRA_NOTIFICATION_ID, notificationId)
            .putExtra(SegmentifyPushActionActivity.EXTRA_ACTION_ID, actionId)
            .putExtra(SegmentifyPushActionActivity.EXTRA_TARGET, target)
        return PendingIntent.getActivity(
            context,
            notificationId * PENDING_INTENTS_PER_NOTIFICATION + slot,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun loadImage(imageUrl: String?): Bitmap? {
        if (imageUrl.isNullOrBlank()) return null
        return try {
            val connection = URL(imageUrl).openConnection() as HttpURLConnection
            connection.connectTimeout = IMAGE_TIMEOUT_MILLIS
            connection.readTimeout = IMAGE_TIMEOUT_MILLIS
            try {
                connection.inputStream.use { BitmapFactory.decodeStream(it) }
            } finally {
                connection.disconnect()
            }
        } catch (e: Exception) {
            SegmentifyLogger.printErrorLog("Push notification image could not be loaded, error: $e")
            null
        }
    }
}
