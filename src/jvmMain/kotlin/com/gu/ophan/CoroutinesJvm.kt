package com.gu.ophan

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val DefaultCoroutineContext: CoroutineContext = Dispatchers.Default

actual class DefaultCoroutineContextFactory {
    actual fun getCoroutineContext(): CoroutineContext {
        return Dispatchers.Default
    }
}