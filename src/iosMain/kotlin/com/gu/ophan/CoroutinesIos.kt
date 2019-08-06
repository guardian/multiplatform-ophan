package com.gu.ophan

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Runnable
import platform.darwin.dispatch_async
import platform.darwin.dispatch_block_t
import platform.darwin.dispatch_get_main_queue
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.freeze

actual val DefaultCoroutineContext: CoroutineContext = MainQueueDispatcher()

class MainQueueDispatcher : CoroutineDispatcher() {

    init {
        println("OphanDispatcher: MainQueueDispatcher created")
    }

    override fun dispatch(context: CoroutineContext, block: Runnable) {
        println("OphanDispatcher: MainQueueDispatcher dispatch method called")
        val queue = dispatch_get_main_queue()
        println("OphanDispatcher: MainQueueDispatcher dispatch method got queue")
        val dispatch_block: dispatch_block_t = {
            println("OphanDispatcher: MainQueueDispatcher block running")
            block.run()
        }//.freeze()
        println("OphanDispatcher: MainQueueDispatcher created dispatch_block_t")
        dispatch_async(queue, dispatch_block)
        println("OphanDispatcher: MainQueueDispatcher called dispatch_async")
    }
}

actual class DefaultCoroutineContextFactory {
    actual fun getCoroutineContext(): CoroutineContext {
        return MainQueueDispatcher()
    }
}
