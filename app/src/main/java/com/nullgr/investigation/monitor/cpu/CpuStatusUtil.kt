package com.nullgr.investigation.monitor.cpu

import android.content.Context
import android.os.Process
import com.nullgr.investigation.utils.Utils

/**
 * @author ovitaliy
 */

object CpuStatusUtil {

    fun getDeviceStatus(): CpuStatus {
        val data = Utils.readFile("/proc/stat")
        val values = data.split(Regex("\\s+"), 9)
        val used = values[1].toLong() + values[2].toLong() + values[3].toLong()
        val total = used + values[4].toLong() + values[5].toLong() + values[6].toLong() + values[7].toLong()

        return CpuStatus(total, used)
    }

    fun getAppStatus(context: Context): Long {
        val pId = getPidFromExec()
        val data = Utils.readFile("/proc/$pId/stat")
        val values = data.split(Regex("\\s+"), 18)

        val used = values[13].toLong() + values[14].toLong() + values[15].toLong() + values[16].toLong()
        return used;
    }


    fun getPidFromExec(): Int? {
        return Process.myPid()
    }


}
