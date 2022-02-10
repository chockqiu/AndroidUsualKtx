package com.chockqiu.usualktx

import com.chockqiu.usualktx.StreamKtx.flowTo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

object FileKtx {

    /**
     * 删除目标目录文件或者文件夹, 包含子文件，不论是否空文件夹
     */
    fun File.deleteAll(): Boolean {
        if (isFile) return delete()
        var s = true
        listFiles()?.forEach { f ->
            if (f.isFile) {
                s = s && f.delete()
            } else {
                s = s && f.deleteChildFiles()
                s = s && f.delete()
            }
        }
        return s && delete()
    }

    /**
     * 删除文件夹内所有文件, 包含子文件，不论是否空文件夹
     */
    fun File.deleteChildFiles(): Boolean {
        if (isFile) return false
        var s = true
        listFiles()?.forEach { f ->
            s = s && f.deleteAll()
        }
        return s
    }

    /**
     * Copy
     *
     * @param file
     */
    fun File.copy2(file: File) {
        FileInputStream(this).flowTo(FileOutputStream(file))
    }
}