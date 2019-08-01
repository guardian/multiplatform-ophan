package com.gu.ophan

import kotlinx.coroutines.*
import platform.darwin.*
import kotlin.coroutines.*

actual val DefaultCoroutineContext: CoroutineContext = MainQueueDispatcher()

class MainQueueDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val queue = dispatch_get_main_queue()
        dispatch_async(queue) {
            block.run()
        }
    }
}
