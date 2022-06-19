package ru.vs.control.server

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.server.feature.dns_sync.featureDnsSync
import ru.vs.control.server.feature.proxy.featureProxy
import ru.vs.control.server.web.KtorClientSSLConfigurationInteractorImpl
import ru.vs.control.server.web.WebServer
import ru.vs.control.server.web.WebServerImpl
import ru.vs.core.coroutines.coreCoroutines
import ru.vs.core.di.Modules
import ru.vs.core.di.i
import ru.vs.core.ktor_client.KtorClientSSLConfigurationInteractor
import ru.vs.core.ktor_client.coreKtorClient
import ru.vs.core.ktor_network.coreKtorNetwork
import ru.vs.core.mikrotik.coreMikrotik
import ru.vs.core.sentry.coreSentry
import ru.vs.core.serialization.coreSerialization

fun createDiGraph() = DI {
    // Core modules
    importOnce(Modules.coreCoroutines())
    importOnce(Modules.coreKtorClient())
    importOnce(Modules.coreKtorNetwork())
    importOnce(Modules.coreMikrotik())
    importOnce(Modules.coreSentry())
    importOnce(Modules.coreSerialization())

    // Feature modules
    importOnce(Modules.featureDnsSync())
    importOnce(Modules.featureProxy())

    // Repositories

    // Domain
    bindSingleton<KtorClientSSLConfigurationInteractor> { KtorClientSSLConfigurationInteractorImpl() }

    // Web server
    bindSingleton<WebServer> { WebServerImpl(i()) }

    // Configurations

    // Api
}