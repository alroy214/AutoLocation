package com.alroy214.autolocation

import android.app.UiModeManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.alroy214.autolocation.LocationFragment.Companion.locationEnable
import com.alroy214.autolocation.MainActivity.Companion.isLocationEnabled


class CarModeReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val action: String = intent.action.toString()
        if (UiModeManager.ACTION_ENTER_CAR_MODE == action) {
            locationEnable(true)
            toastLocation(context)
        } else if (UiModeManager.ACTION_EXIT_CAR_MODE == action) {
            locationEnable(false)
            toastLocation(context)
        }
    }
    private fun toastLocation(context: Context) {
        Toast.makeText(context, "Location is now " + if (isLocationEnabled(context))
            "enabled" else "disabled", Toast.LENGTH_SHORT).show()
    }
}