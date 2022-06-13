package ru.vs.core.sentry

import io.sentry.Sentry
import ru.vs.core.crash_handler.CrashHandlerAbstract

internal class SentryCrashHandler : CrashHandlerAbstract() {
    override fun recordExceptionInternal(t: Throwable) {
        Sentry.captureException(t)
    }
}