package com.nullgr.investigation.monitor.battery

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager


/**
 * @author ovitaliy
 */

object BatteryStatusUtil {
    fun getStatus(context: Context): Double {
        val ifilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, ifilter)
        val level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        val scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);

        return level.toDouble() / scale.toDouble();

    }
}
