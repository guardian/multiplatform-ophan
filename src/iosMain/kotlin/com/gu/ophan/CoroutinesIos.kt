package com.gu.ophan

import kotlinx.coroutines.*
import platform.darwin.*
import kotlin.coroutines.*
import kotlin.native.concurrent.freeze

@SharedImmutable
actual val DefaultCoroutineContext: CoroutineContext = MainQueueDispatcher()

class MainQueueDispatcher : CoroutineDispatcher() {
    override fun dispatch(context: CoroutineContext, block: Runnable) {
        val queue = dispatch_get_main_queue()
        val frozenBlock = block.freeze()
        dispatch_async(queue) {
            frozenBlock.run()
        }
    }
}
