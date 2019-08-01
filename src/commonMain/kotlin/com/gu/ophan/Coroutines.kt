package com.gu.ophan

import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
expect val DefaultCoroutineContext: CoroutineContext