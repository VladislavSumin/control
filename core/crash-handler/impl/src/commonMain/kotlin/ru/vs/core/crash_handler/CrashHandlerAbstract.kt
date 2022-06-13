package ru.vs.core.crash_handler

import co.touchlab.kermit.Logger

// TODO разделить модуль на api/impl
abstract class CrashHandlerAbstract : CrashHandler {

    final override fun recordException(t: Throwable) {
        logger.w("Recording non fatal exception", t)
        recordExceptionInternal(t)
    }

    protected abstract fun recordExceptionInternal(t: Throwable)

    companion object {
        val logger = Logger.withTag("CrashHandler")
    }
}