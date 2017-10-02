package com.nullgr.investigation.utils

import java.text.DecimalFormat

/**
 * @author ovitaliy
 */

fun String.toDecimalFormatted(): String {
    val format = DecimalFormat("#.00")
    return format.format(this.toDouble())
}
