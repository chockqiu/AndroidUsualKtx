package com.chockqiu.usualktx

import java.io.*
import java.nio.charset.Charset

object StreamKtx {

    fun InputStream.flowTo(out: OutputStream, close: Boolean? = true) {
        val buf = ByteArray(1024 * 4)
        var c: Int = read(buf)
        while (c != -1) {
            out.write(buf, 0, c)
            c = read(buf)
        }
        if (close == true) {
            close()
            out.close()
        }
    }

    fun InputStream.saveAsFile(file: File, close: Boolean? = true): File {
        val out = FileOutputStream(file)
        flowTo(out, close)
        return file
    }

    fun InputStream.readAsBytes(close: Boolean? = true): ByteArray {
        val out = ByteArrayOutputStream(1024 * 4)
        flowTo(out, close)
        return out.toByteArray()
    }

    fun InputStream.readAsStr(charset: String = "utf-8", close: Boolean? = true): String {
        return String(readAsBytes(close), Charset.forName(charset))
    }
}