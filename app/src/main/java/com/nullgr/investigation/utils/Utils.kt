package com.nullgr.investigation.utils

import org.apache.commons.io.IOUtils
import java.io.FileReader
import java.nio.charset.Charset

/**
 * @author ovitaliy
 */
object Utils {

    fun execCommand(command: String) =
            Runtime.getRuntime().exec(command).let {
                val inputStream = it.getInputStream();
                val result = IOUtils.toString(inputStream, Charset.defaultCharset())
                inputStream.close()
                result
            }

    fun readFile(file: String) =
            FileReader(file).let {
                val result = IOUtils.toString(it)
                it.close()
                result
            }

}