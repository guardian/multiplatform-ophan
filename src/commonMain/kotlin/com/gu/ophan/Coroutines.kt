package com.gu.ophan

import kotlin.coroutines.CoroutineContext

expect val DefaultCoroutineContext: CoroutineContext

expect class DefaultCoroutineContextFactory() {
    fun getCoroutineContext(): CoroutineContext
}