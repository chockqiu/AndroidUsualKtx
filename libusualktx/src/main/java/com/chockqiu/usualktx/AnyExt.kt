package com.chockqiu.usualktx

object AnyExt {

    inline fun Any?.catchBlock(block: () -> Unit): Exception? {
        try {
            block.invoke()
        } catch (e: Exception) {
            e.printStackTrace()
            return e
        }
        return null
    }
}