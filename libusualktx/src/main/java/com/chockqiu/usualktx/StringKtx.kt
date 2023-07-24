package com.chockqiu.usualktx

import java.security.MessageDigest

object StringKtx {
    /**
     * 获取md5
     * @receiver String
     * @param upperCase Boolean
     * @return String
     */
    fun String.getMD5(upperCase: Boolean = true): String {
        var md5str = ""
        try {
            val md = MessageDigest.getInstance("MD5")
            val input = this.toByteArray()
            val buff = md.digest(input)
            md5str = bytesToHex(buff, upperCase)
        } catch (var6: Exception) {
            var6.printStackTrace()
        }
        return md5str
    }

    /**
     * 二进制转字符串
     * @param bytes ByteArray
     * @param upperCase Boolean
     * @return String
     */
    fun bytesToHex(bytes: ByteArray, upperCase: Boolean): String {
        val md5str = StringBuffer()
        for (i in bytes.indices) {
            var digital = bytes[i].toInt()
            if (digital < 0) {
                digital += 256
            }
            if (digital < 16) {
                md5str.append("0")
            }
            md5str.append(Integer.toHexString(digital))
        }
        return if (upperCase) {
            md5str.toString().toUpperCase()
        } else {
            md5str.toString().toLowerCase()
        }
    }
}