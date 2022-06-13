package ru.vs.core.crash_handler

interface CrashHandler {
    fun recordException(t: Throwable)
}