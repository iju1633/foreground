package com.jaeuk.foreground

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager

class PhoneCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val state = telephonyManager.callState
            if (state == TelephonyManager.CALL_STATE_RINGING) {
                // 전화가 수신되면 MainActivity 호출
                val mainActivityIntent = Intent(context, MainActivity::class.java)
                mainActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(mainActivityIntent)
            }
        }
    }
}