package ru.vs.control.server.web

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import ru.vs.control.server.feature.proxy.web.ProxyModule

private const val SERVER_DEFAULT_PORT = 8080

interface WebServer {
    suspend fun run()
}

class WebServerImpl(
    private val proxyModule: ProxyModule,
) : WebServer {
    override suspend fun run() {
        withContext(CoroutineName("web-server")) {
            val environment = createEnvironment()
            val server = createEmbeddedServer(environment)
            server.start(true)
        }
    }
    private fun CoroutineScope.createEnvironment(): ApplicationEngineEnvironment {
        return applicationEngineEnvironment {
            // Append parent coroutine context
            parentCoroutineContext = coroutineContext

            connector {
                host = "0.0.0.0"
                port = SERVER_DEFAULT_PORT
            }

            proxyModule.apply { install() }
        }
    }

    private fun createEmbeddedServer(environment: ApplicationEngineEnvironment): ApplicationEngine =
        embeddedServer(Netty, environment)
}
