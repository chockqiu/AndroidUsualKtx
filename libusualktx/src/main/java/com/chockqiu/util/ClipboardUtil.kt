package com.chockqiu.util

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context


/**
 * 剪切板
 */
@SuppressLint("StaticFieldLeak")
object ClipboardUtil {

    lateinit var mContext: Context

    fun init(context: Context) {
        mContext = context.applicationContext
    }

    fun copy(text: String) {
        val cmb: ClipboardManager =
            mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        //创建ClipData对象
        val clipData: ClipData = ClipData.newPlainText("AppCopyClip", text)
        //添加ClipData对象到剪切板中
        cmb.setPrimaryClip(clipData)
    }

    fun getClipboardString(): String? {
        val clipboardManager =
            mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            return clipData.getItemAt(0)?.text?.toString()
        }
        return null
    }

    fun cleanClipboard() {
        val cmb: ClipboardManager =
            mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData: ClipData = ClipData.newPlainText("AppCopyClip2", "")
        //添加ClipData对象到剪切板中
        cmb.setPrimaryClip(clipData)
    }
}