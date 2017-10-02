package com.nullgr.investigation.mem

/**
 * @author ovitaliy
 */
data class MemStatus(val total: Long,
                     val used: Long
) {
    val usedPercent: Double
        get() = used.toDouble() / total.toDouble() * 100.0

    override fun toString(): String {
        return "MemStatus(total=$total, used=$used, usedPercent=$usedPercent, )"
    }


}