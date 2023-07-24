package com.chockqiu.usualktx

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

object CoroutineScopeKtx {

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


    fun CoroutineScope.launchIOCatch(
        context: CoroutineContext = Dispatchers.IO,
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

    fun CoroutineScope.launchDefaultCatch(
        context: CoroutineContext = Dispatchers.Default,
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

    fun CoroutineScope.launchMainCatch(
        context: CoroutineContext = Dispatchers.Main,
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

    suspend fun withContextMain(
        block: suspend CoroutineScope.() -> Unit
    ) {
        withContext(Dispatchers.Main) {
            block()
        }
    }

    suspend fun withContextDefault(
        block: suspend CoroutineScope.() -> Unit
    ) {
        withContext(Dispatchers.Default) {
            block()
        }
    }

    suspend fun withContextIO(
        block: suspend CoroutineScope.() -> Unit
    ) {
        withContext(Dispatchers.IO) {
            block()
        }
    }
}