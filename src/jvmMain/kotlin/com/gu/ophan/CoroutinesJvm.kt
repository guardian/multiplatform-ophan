package com.gu.ophan

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual fun getDefaultCoroutineContext(): CoroutineContext {
    return Dispatchers.Default
}
