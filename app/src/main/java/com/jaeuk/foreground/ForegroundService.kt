package com.jaeuk.foreground

import android.app.*
import android.app.PendingIntent.*
import android.content.Intent
import android.graphics.Color
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// 사용자에게 Notification 전달하는 서비스
class ForegroundService : Service() {

    private val NOTIFICATION_ID = 1
    private val NOTIFICATION_CHANNEL_ID = "ForegroundServiceChannel"
    private val NOTIFICATION_CHANNEL_NAME = "name"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        if (isNotificationPermissionGranted()) {
            val notificationIntent = Intent(this, MainActivity::class.java)
            val pendingIntent = getActivity(
                this,
                0,
                notificationIntent,
                FLAG_IMMUTABLE // api level 31부터 필요
            )
            val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Foreground Service")
                .setContentText("Running")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pendingIntent)
                .setColor(Color.BLUE)
                .build()

            startForeground(NOTIFICATION_ID, notification)
        }
        return START_STICKY
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }

    private fun isNotificationPermissionGranted(): Boolean {
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        return notificationManagerCompat.areNotificationsEnabled()
    }
}