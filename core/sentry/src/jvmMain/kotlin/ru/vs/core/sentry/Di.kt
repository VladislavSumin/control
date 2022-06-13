package ru.vs.core.sentry

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.core.crash_handler.CrashHandler
import ru.vs.core.di.Modules

fun Modules.coreSentry() = DI.Module("core-sentry") {
    bindSingleton<CrashHandler> { SentryCrashHandler() }
}