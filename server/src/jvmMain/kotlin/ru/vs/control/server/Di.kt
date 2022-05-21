package ru.vs.control.server

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.server.web.WebServer
import ru.vs.control.server.web.WebServerImpl

fun createDiGraph() = DI {
    // Core modules

    // Feature modules

    // Repositories

    // Domain

    // Web server
    bindSingleton<WebServer> { WebServerImpl() }

    // Configurations

    // Api
}