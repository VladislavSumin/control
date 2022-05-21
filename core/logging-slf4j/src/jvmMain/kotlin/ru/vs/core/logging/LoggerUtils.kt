package ru.vs.core.logging

import co.touchlab.kermit.Logger
import org.apache.logging.log4j.LogManager

fun Logger.setupDefault() {
    Logger.setLogWriters(KermitLog4jWriter())
}

/**
 * Log4j2 logger doesn't close automatically. We must close them manually.
 */
fun Logger.shutdown() {
    LogManager.shutdown()
}
