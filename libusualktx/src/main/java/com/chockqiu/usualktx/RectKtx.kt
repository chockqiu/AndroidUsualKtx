package com.chockqiu.usualktx

import android.graphics.Rect
import kotlin.math.max
import kotlin.math.min

/**
 * 按照屏幕坐标系 囊括另一个矩形得到新的矩形
 *
 * @param another
 */
fun Rect.join(another: Rect) {
    left = min(another.left, left)
    top = min(another.top, top)
    right = max(another.right, right)
    bottom = max(another.bottom, bottom)
}