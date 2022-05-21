package ru.vs.control.server

import co.touchlab.kermit.Logger
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.vs.core.logging.setupDefault
import ru.vs.core.logging.shutdown

fun main() {
    // Setup logger
    Logger.setupDefault()
    Logger.setTag("server")

    Logger.i("Starting server")

    // Create server scope
    val serverScope = ServerScope(::closeLogger)

    // Create DI
    val di = createDiGraph()

    serverScope.launch {
        delay(Long.MAX_VALUE)
    }

    // coroutines use daemon thread, we must keep main thread alive
    serverScope.blockingAwait()
}

private fun closeLogger() {
    Logger.i("Shooting down logger")
    Logger.shutdown()
}
