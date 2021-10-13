package com.chockqiu.usualktx

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * 有try-catch的携程调用
 *
 * @param context
 * @param start
 * @param fail
 * @param block
 * @receiver
 */
fun CoroutineScope.launchCatch(
    context: CoroutineContext = coroutineContext,
    start: CoroutineStart = CoroutineStart.DEFAULT,
    fail: (suspend CoroutineScope.(e: Throwable) -> Unit)? = null,
    block: suspend CoroutineScope.() -> Unit
) = launch(context, start) {
    try {
        this.block()
    } catch (e: Throwable) {
        e.printStackTrace()
        fail?.invoke(this, e)
    }
}