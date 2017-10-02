package com.nullgr.investigation.monitor

import android.content.Context
import com.nullgr.investigation.mem.MemStatusUtil
import com.nullgr.investigation.monitor.battery.BatteryStatusUtil
import com.nullgr.investigation.monitor.cpu.CpuStatusUtil
import java.util.*
import kotlin.collections.LinkedHashMap

/**
 * @author ovitaliy
 */
object Monitor {

    const val time = "time"
    const val cpuTotal = "cpuTotal"
    const val cpuUsed = "cpuUsed"
    const val cpuUsedPercent = "cpuUsed%"
    const val cpuAppUsed = "cpuAppUsed"
    const val cpuAppUsedPercent = "cpuAppUsed%"
    const val memory = "memory"
    const val memoryUsed = "memoryUsed"
    const val memoryUsedPercent = "memoryUsed%"
    const val memoryApp = "memoryApp"
    const val memoryAppUsed = "memoryAppUsed%"
    const val memoryAppUsedPercent = "memoryAppUsed%"
    const val battery = "battery"

    fun getMotitoredData(context: Context): HashMap<String, String> {

        val dataMap = LinkedHashMap<String, String>()
        dataMap.put("time", Date().toString())

        val deviceCpu = CpuStatusUtil.getDeviceStatus();
        dataMap.put(cpuTotal, deviceCpu.total.toString())
        dataMap.put(cpuUsed, deviceCpu.used.toString())
        dataMap.put(cpuUsedPercent, deviceCpu.usedPercent.toString())

        val cpuAppStatus = CpuStatusUtil.getAppStatus(context)
        dataMap.put(cpuAppUsed, cpuAppStatus.toString())
        dataMap.put(cpuAppUsedPercent, (cpuAppStatus * 100.0 / deviceCpu.total).toString())


        val deviceMemory = MemStatusUtil.getDeviceMemoryStatus(context)
        dataMap.put(memory, deviceMemory.total.toString())
        dataMap.put(memoryUsed, deviceMemory.used.toString())
        dataMap.put(memoryUsedPercent, deviceMemory.usedPercent.toString())

        val appMemory = MemStatusUtil.getAppMemoryStatus()
        dataMap.put(memoryApp, appMemory.total.toString())
        dataMap.put(memoryAppUsed, appMemory.used.toString())
        dataMap.put(memoryAppUsedPercent, appMemory.usedPercent.toString())

        dataMap.put(battery, BatteryStatusUtil.getStatus(context).toString())
        return dataMap
    }


}