package com.chockqiu.usualktx

import android.app.Application
import com.chockqiu.usualktx.AnyExt.catchBlock
import java.io.File
import java.security.MessageDigest

object StringKtx {

    lateinit var mContext: Application

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

    fun String?.isNull(): Boolean {
        if (this.isNullOrBlank()) {
            return true
        }
        if (this.trim() == "null") {
            return true
        }
        return false
    }

    fun String?.isNullOrZero(): Boolean {
        if (this.isNullOrBlank()) {
            return true
        }
        if (this.trim() == "null") {
            return true
        }
        if (this.trim() == "0") {
            return true
        }
        return false
    }

    //eg: jpg png  不包含.
    fun String.urlGetFileExt(): String {
        val s = lastIndexOf(".")
        if (s >= 0 && s + 1 <= length) {
            val x = lastIndexOf("?")
            if (x > s + 1) {
                return substring(s + 1, x)
            }
            return substring(s + 1)
        }
        return "ncache"
    }

    /**
     * 将Url转换成缓存文件路径(不一定存在), 但一一对应
     */
    fun String.urlCacheFile(): File {
        val f = getMD5(true)
        val imgRoot = File(mContext.cacheDir, "fic")
        catchBlock { if (!imgRoot.exists()) imgRoot.mkdirs() }
        return File(imgRoot, "${f}.${urlGetFileExt()}")
    }

//    /**
//     * 将Url缓存为文件
//     */
//    fun String.urlCacheAsFile() {
//        val url = this.trim()
//        if (url.isBlank()) return
//        GlobalScope.launchDefaultCatch {
//            urlCacheFile().let {
//                if (!it.exists()) {
//                    val s = FileUtil.downFile(url, it)
//                    "FileUtil.downFile complete result=$s file = ${it.path}".debugLog("@@")
//                }
//            }
//        }
//    }

}