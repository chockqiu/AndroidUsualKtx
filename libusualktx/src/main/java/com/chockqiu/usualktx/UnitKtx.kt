package com.chockqiu.usualktx

import android.app.Application
import android.content.Context
import android.util.TypedValue

object UnitKtx {

    lateinit var mContext: Application

    fun Float.dpToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Int.dpToPx(context: Context? = null): Int {
        val mContext = context ?: mContext
        return (TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f).toInt()
    }

    fun Float.pxToDp(context: Context? = null): Float {
        val mContext = context ?: mContext
        val scale: Float = mContext.resources.displayMetrics.density
        return this.toFloat() / scale + 0.5f
    }

    fun Float.spToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Float.pxToSP(context: Context? = null): Float {
        val mContext = context ?: mContext
        val fontScale = mContext.resources.displayMetrics.scaledDensity
        return this.toFloat() / fontScale + 0.5f
    }

    fun Float.ptToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Float.inToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_IN,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Float.mmToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_MM,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }
}