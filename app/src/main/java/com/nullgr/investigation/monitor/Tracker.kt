package com.nullgr.investigation.monitor

import android.content.Context
import android.util.Log
import java.io.File
import java.util.*

/**
 * @author ovitaliy
 */

class Tracker(private val type: String) {

    fun track(data: HashMap<String, String>, context: Context, count: Int) {
        val f = File(context.externalCacheDir, getFileName(type, count)).apply {
            if (!exists()) createNewFile()
            appendText(data.keys.joinToString(",") + "\n")
        }

        val values = data.values.joinToString(",") + "\n"
        f.appendText(values)

        val traceValues = arrayListOf(
                data.get(Monitor.cpuUsedPercent),
                data.get(Monitor.cpuAppUsed),
                data.get(Monitor.memoryAppUsedPercent)
        )

        Log.e("Tracker", "$type " + traceValues.joinToString(","))
    }

    private fun getFileName(type: String, count: Int)
            = "$type $count.csv"


}
