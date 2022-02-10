package com.chockqiu.usualktx

import android.animation.ValueAnimator
import android.view.animation.AccelerateInterpolator
import android.widget.TextView

object TextViewKtx {
    /**
     * 数字跳动动画函数
     *
     * @param number 整数或者浮点数
     * @param duration 动画时长
     */
    fun TextView.startNumberDanceAnimation(number: Number, duration: Long) {
        val ani = ValueAnimator.ofFloat(0f, number.toFloat()).setDuration(duration)
        val format = "%1\$01.${if (number is Float) "2" else "0"}f"
        ani.interpolator = AccelerateInterpolator()
        ani.addUpdateListener {
            text = String.format(format, it.animatedValue)
        }
        ani.start()
    }
}