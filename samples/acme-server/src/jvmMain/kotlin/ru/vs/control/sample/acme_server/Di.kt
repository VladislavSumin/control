package ru.vs.control.sample.acme_server

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.sample.acme_server.web.WebServer
import ru.vs.control.sample.acme_server.web.WebServerImpl
import ru.vs.core.coroutines.coreCoroutines
import ru.vs.core.di.Modules
import ru.vs.core.ktor_client.coreKtorClient
import ru.vs.core.ktor_network.coreKtorNetwork
import ru.vs.core.serialization.coreSerialization

fun createDiGraph() = DI {
    // Core modules
    importOnce(Modules.coreCoroutines())
    importOnce(Modules.coreKtorClient())
    importOnce(Modules.coreKtorNetwork())
    importOnce(Modules.coreSerialization())

    // Web server
    bindSingleton<WebServer> { WebServerImpl() }
}