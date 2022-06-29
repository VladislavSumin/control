package ru.vs.control.sample.acme_server

import co.touchlab.kermit.Logger
import kotlinx.coroutines.DEBUG_PROPERTY_NAME
import kotlinx.coroutines.launch
import org.kodein.di.direct
import org.kodein.di.instance
import ru.vs.control.sample.acme_server.web.WebServer
import ru.vs.core.coroutines.ServerScope
import ru.vs.core.logging.setupDefault
import ru.vs.core.logging.shutdown

fun main() {
    // Setup logger
    Logger.setupDefault()
    Logger.setTag("server")

    Logger.i("Starting server")

    // Enable coroutine debug
    System.setProperty(DEBUG_PROPERTY_NAME, "on")

    // Create server scope
    val serverScope = ServerScope(::closeLogger)

    val di = createDiGraph()

    serverScope.launch {

        // Start web server (blocking)
        di.direct.instance<WebServer>().run()
    }

    // coroutines use daemon thread, we must keep main thread alive
    serverScope.blockingAwait()
}

private fun closeLogger() {
    Logger.i("Shooting down logger")
    Logger.shutdown()
}
