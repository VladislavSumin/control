package ru.vs.control.server

import co.touchlab.kermit.Logger
import io.sentry.Sentry
import kotlinx.coroutines.launch
import org.kodein.di.direct
import org.kodein.di.instance
import ru.vs.control.server.web.WebServer
import ru.vs.core.logging.setupDefault
import ru.vs.core.logging.shutdown

fun main() {
    // Setup logger
    Logger.setupDefault()
    Logger.setTag("server")

    Logger.i("Starting server")

    Sentry.init { options ->
        options.dsn = "https://2bad5747065c4d64b7cdfa70c64f80fc@o512687.ingest.sentry.io/6495152"
        options.tracesSampleRate = 1.0
        options.isDebug = true
    }

    // Create server scope
    val serverScope = ServerScope(::closeLogger)

    // Create DI
    val di = createDiGraph()

    // Start web server
    serverScope.launch { di.direct.instance<WebServer>().run() }

    // coroutines use daemon thread, we must keep main thread alive
    serverScope.blockingAwait()
}

private fun closeLogger() {
    Logger.i("Shooting down logger")
    Logger.shutdown()
}
