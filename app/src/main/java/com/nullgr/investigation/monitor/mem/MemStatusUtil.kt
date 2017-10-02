package com.nullgr.investigation.mem

import android.app.ActivityManager
import android.content.Context
import android.content.Context.ACTIVITY_SERVICE


/**
 * @author ovitaliy
 */
object MemStatusUtil {


    //https://stackoverflow.com/a/3192348
    fun getDeviceMemoryStatus(context: Context): MemStatus {
        val mi = ActivityManager.MemoryInfo()
        val activityManager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        activityManager.getMemoryInfo(mi)

        return MemStatus(mi.totalMem / 0x1000L, (mi.totalMem - mi.availMem) / 0x1000L)
    }

    //https://stackoverflow.com/a/19267315
    fun getAppMemoryStatus(): MemStatus {
        val info = Runtime.getRuntime()
        val total = info.totalMemory() / 0x1000L
        val free = info.freeMemory() / 0x1000L
        return MemStatus(total, total - free)
    }

}