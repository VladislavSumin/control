package ru.vs.control.sample.acme_server.web

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import ru.vs.core.utils.network.KeyStoreUtils
import java.io.File

private const val SERVER_DEFAULT_PORT = 8080

interface WebServer {
    suspend fun run()
}

class WebServerImpl() : WebServer {
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

            val keystore = KeyStoreUtils.readJks(
                File("config/ssl/control.vs.pem"),
                File("config/ssl/control.vs.crt"),
                File("config/ssl/intermediate_ca.crt"),
            )
            sslConnector(keystore, "key", { "".toCharArray() }, { "".toCharArray() }) {
                port = 8443
                host = "0.0.0.0"
            }

            module {
                install(CallLogging)
            }

//            proxyModule.apply { install() }
        }
    }

    private fun createEmbeddedServer(environment: ApplicationEngineEnvironment): ApplicationEngine =
        embeddedServer(Netty, environment)
}
