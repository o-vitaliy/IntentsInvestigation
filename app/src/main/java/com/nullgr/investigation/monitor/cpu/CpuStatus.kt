package com.nullgr.investigation.monitor.cpu

/**
 * @author ovitaliy
 */
data class CpuStatus(val total: Long,
                     val used: Long) {

    val usedPercent: Double
        get() = restrictPercentage(used.toDouble()  * 100.0 / total.toDouble())

    private fun restrictPercentage(percentage: Double): Double {
        return if (percentage > 100)
            100.0
        else if (percentage < 0)
            0.0
        else
            percentage
    }

    override fun toString(): String {
        return "CpuStatus(total=$total, used=$used, usedPercent=$usedPercent)"
    }

}
