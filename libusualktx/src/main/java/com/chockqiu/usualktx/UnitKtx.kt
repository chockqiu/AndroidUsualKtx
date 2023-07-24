package com.chockqiu.usualktx

import android.app.Application
import android.content.Context
import android.util.TypedValue

object UnitKtx {

    lateinit var mContext: Application

    fun Number.dpToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Number.pxToDp(context: Context? = null): Float {
        val mContext = context ?: mContext
        val scale: Float = mContext.resources.displayMetrics.density
        return this.toFloat() / scale + 0.5f
    }

    fun Number.spToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Number.pxToSP(context: Context? = null): Float {
        val mContext = context ?: mContext
        val fontScale = mContext.resources.displayMetrics.scaledDensity
        return this.toFloat() / fontScale + 0.5f
    }

    fun Number.ptToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_PT,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Number.inToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_IN,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }

    fun Number.mmToPx(context: Context? = null): Float {
        val mContext = context ?: mContext
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_MM,
            this.toFloat(),
            mContext.resources.displayMetrics
        ) + 0.5f
    }
}