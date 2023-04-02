package com.jaeuk.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

// 디바이스 부팅 시, Foreground 서비스 시작
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val serviceIntent = Intent(context, ForegroundService::class.java)
            ContextCompat.startForegroundService(context, serviceIntent)
        }
    }
}