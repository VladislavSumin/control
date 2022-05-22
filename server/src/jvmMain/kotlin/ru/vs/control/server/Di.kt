package ru.vs.control.server

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.server.feature.proxy.featureProxy
import ru.vs.control.server.web.WebServer
import ru.vs.control.server.web.WebServerImpl
import ru.vs.core.di.Modules
import ru.vs.core.di.i

fun createDiGraph() = DI {
    // Core modules

    // Feature modules
    importOnce(Modules.featureProxy())

    // Repositories

    // Domain

    // Web server
    bindSingleton<WebServer> { WebServerImpl(i()) }

    // Configurations

    // Api
}