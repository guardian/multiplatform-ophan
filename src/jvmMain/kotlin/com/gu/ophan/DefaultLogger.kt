package com.gu.ophan

import java.util.logging.Level
import java.util.logging.Logger

actual class DefaultLogger : com.gu.ophan.Logger {
    override fun debug(tag: String, message: String) {
        Logger.getLogger(tag).log(Level.FINE, message)
    }

    override fun warn(tag: String, message: String, error: Throwable?) {
        Logger.getLogger(tag).log(Level.WARNING, message, error)
    }
}
