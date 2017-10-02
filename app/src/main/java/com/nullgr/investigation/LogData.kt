package com.nullgr.investigation

import com.nullgr.investigation.monitor.cpu.CpuStatus
import com.nullgr.investigation.mem.MemStatus

/**
 * @author ovitaliy
 */
data class LogData(
        val dispatchedAt: Long,
        val memStatus: MemStatus,
        val cpuStatus: CpuStatus,
        val batteryStatus: Double,
        val intentId: Long
)