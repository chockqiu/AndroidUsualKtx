package com.chockqiu.usualktx

import android.animation.Animator
import android.view.ViewPropertyAnimator

object AnimatorKtx {

    inline fun ViewPropertyAnimator.addListener(
        crossinline onEnd: (animator: Animator) -> Unit = {},
        crossinline onStart: (animator: Animator) -> Unit = {},
        crossinline onCancel: (animator: Animator) -> Unit = {},
        crossinline onRepeat: (animator: Animator) -> Unit = {}
    ): ViewPropertyAnimator {
        setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                onStart(animator)
            }

            override fun onAnimationEnd(animator: Animator) {
                onEnd(animator)
            }

            override fun onAnimationCancel(animator: Animator) {
                onCancel(animator)
            }

            override fun onAnimationRepeat(animator: Animator) {
                onRepeat(animator)
            }
        })
        return this
    }
}