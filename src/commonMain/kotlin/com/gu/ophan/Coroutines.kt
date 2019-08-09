package com.gu.ophan

import kotlin.coroutines.CoroutineContext

expect class DefaultCoroutineContextFactory() {
    fun getCoroutineContext(): CoroutineContext
}