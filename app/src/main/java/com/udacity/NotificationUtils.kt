package com.udacity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

private const val CHANNEL_ID = "channelId"
private const val NOTIFICATION_ID = 0
const val EXTRA_PROJECT_NAME = "ExtraProjectName"
const val EXTRA_DOWNLOAD_STATUS = "ExtraDownloadStatus"

@RequiresApi(Build.VERSION_CODES.O)
fun createNotificationChannel(context: Context) {
    val notificationChannel = NotificationChannel(
        CHANNEL_ID,
        context.getString(R.string.app_name),
        NotificationManager.IMPORTANCE_DEFAULT
    )

    notificationChannel.apply {
        enableLights(true)
        lightColor = Color.RED
        enableVibration(true)
        description = context.getString(R.string.download_files_complete)
        setShowBadge(false)
    }

    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.createNotificationChannel(notificationChannel)
}

fun NotificationManager.sendNotification(context: Context, projectName: String, downloadStatus: String) {
    // The notification should launch the DetailActivity when clicked
    val detailIntent = Intent(context, DetailActivity::class.java)
    detailIntent.putExtra(EXTRA_PROJECT_NAME, projectName)
    detailIntent.putExtra(EXTRA_DOWNLOAD_STATUS, downloadStatus)

    val detailPendingIntent = PendingIntent.getActivity(
        context,
        NOTIFICATION_ID,
        detailIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(
        context, CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_assistant_black_24dp)
        .setContentTitle(context.getString(R.string.notification_title))
        .setContentText(context.getString(R.string.notification_description))
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            context.getString(R.string.notification_button),
            detailPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}

fun cancelNotification(context: Context) {
    val notificationManager = context.getSystemService(NotificationManager::class.java)
    notificationManager.cancelAll()
}