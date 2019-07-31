package com.gu.ophan

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

actual val DefaultCoroutineContext: CoroutineContext = Dispatchers.Default