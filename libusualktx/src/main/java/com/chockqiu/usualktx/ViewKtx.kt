package com.chockqiu.usualktx

import android.graphics.Rect
import android.view.View

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
