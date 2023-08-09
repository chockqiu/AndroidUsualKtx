package com.chockqiu.util

import android.content.Context
import android.view.View
import com.chockqiu.usualktx.UnitKtx.dpToPx
import com.chockqiu.usualktx.ViewKtx.getMarginLayoutParams
import com.xy.xframework.extensions.ContextExt.getScreenHeight
import com.xy.xframework.extensions.ContextExt.getScreenWidth

class DpSizeUtils(private val context: Context) {

    private val rw by lazy { context.getScreenWidth() / 375f.dpToPx() }
    private val rh by lazy { context.getScreenHeight() / 812f.dpToPx() }

    fun applyViews(vs: List<DpView>) {
        vs.forEach { mDpView ->
            mDpView.apply {
                view?.let { v ->
                    width?.let {
                        v.layoutParams.width = getWidthDpValue(it)
                    }
                    height?.let {
                        v.layoutParams.height = getHeightDpValue(it)
                    }
                    leftMargin?.let {
                        v.getMarginLayoutParams()?.leftMargin = getWidthDpValue(it)
                    }
                    topMargin?.let {
                        v.getMarginLayoutParams()?.topMargin = getHeightDpValue(it)
                    }
                    rightMargin?.let {
                        v.getMarginLayoutParams()?.rightMargin = getWidthDpValue(it)
                    }
                    bottomMargin?.let {
                        v.getMarginLayoutParams()?.bottomMargin = getHeightDpValue(it)
                    }
                    v.requestLayout()
                }
            }
        }
    }

    fun getWidthDpValue(dp: Int): Int {
        return (rw * dp.dpToPx()).toInt()
    }

    fun getHeightDpValue(dp: Int): Int {
        return (rh * dp.dpToPx()).toInt()
    }
}

class DpView {
    var view: View? = null
    var width: Int? = null
    var height: Int? = null
    var leftMargin: Int? = null
    var topMargin: Int? = null
    var rightMargin: Int? = null
    var bottomMargin: Int? = null
}