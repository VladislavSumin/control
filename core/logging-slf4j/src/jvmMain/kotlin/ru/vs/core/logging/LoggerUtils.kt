package ru.vs.core.logging

import co.touchlab.kermit.Logger
import org.apache.logging.log4j.LogManager

fun Logger.setupDefault() {
    Logger.setLogWriters(KermitLog4jWriter())
}

/**
 * log4j использует свои потоки для записи логов
 * Поэтому процесс не завершиться корректно если не завершить работу логера
 */
fun Logger.shutdown() {
    LogManager.shutdown()
}
