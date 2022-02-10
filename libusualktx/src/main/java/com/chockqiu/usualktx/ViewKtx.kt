package com.chockqiu.usualktx

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import com.chockqiu.usualktx.RectKtx.join


object ViewKtx {
    /**
     * 扩展方法：设置点击事件，防止快速点击
     */
    fun View.setSingleClick(ignoreTime: Int = 500, onclick: (View) -> Unit) {
        var mLastClickMills = 0L
        setOnClickListener {
            val currentTimeMillis = System.currentTimeMillis()
            if (currentTimeMillis - mLastClickMills >= ignoreTime) {
                mLastClickMills = currentTimeMillis
                onclick.invoke(this)
            }
        }
    }

    /**
     * Get global visible rect with another views
     *
     * @param views 需要参与计算的View
     * @return
     */
    fun View.getGlobalVisibleRectWith(vararg views: View): Rect {
        val mRect = Rect()
        this.getGlobalVisibleRect(mRect)
        views.forEach { view ->
            val m = Rect()
            view.getGlobalVisibleRect(m)
            mRect.join(m)
        }
        return mRect
    }

    fun View.setMargins(left: Int, top: Int, right: Int, bottom: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.setMargins(left, top, right, bottom)
        }
    }

    fun View.marginStart(px: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.marginStart = px
        }
    }

    fun View.marginLeft(px: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.leftMargin = px
        }
    }

    fun View.marginTop(px: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.topMargin = px
        }
    }

    fun View.marginEnd(px: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.marginEnd = px
        }
    }

    fun View.marginRight(px: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.rightMargin = px
        }
    }

    fun View.marginBottom(px: Int) {
        val lp = layoutParams
        if (lp is ViewGroup.MarginLayoutParams) {
            lp.bottomMargin = px
        }
    }
}