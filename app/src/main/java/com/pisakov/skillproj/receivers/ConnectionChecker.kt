package com.pisakov.skillproj.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.pisakov.skillproj.R

class ConnectionChecker : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_BATTERY_LOW -> Toast.makeText(context, context?.getString(R.string.battary_low), Toast.LENGTH_SHORT).show()
            Intent.ACTION_POWER_CONNECTED -> Toast.makeText(context, context?.getString(R.string.charging_connected), Toast.LENGTH_SHORT).show()
        }
    }
}