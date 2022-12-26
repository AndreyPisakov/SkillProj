package com.pisakov.skillproj.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class ConnectionChecker : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> Toast.makeText(context, "Батарея разряжена", Toast.LENGTH_SHORT).show()
            Intent.ACTION_POWER_CONNECTED -> Toast.makeText(context, "Зарядка подключена", Toast.LENGTH_SHORT).show()
        }
    }
}