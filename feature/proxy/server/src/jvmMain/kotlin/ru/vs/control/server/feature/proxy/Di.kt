package ru.vs.control.server.feature.proxy

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.server.feature.proxy.domain.ProxyConfigurationInteractor
import ru.vs.control.server.feature.proxy.domain.ProxyConfigurationInteractorImpl
import ru.vs.control.server.feature.proxy.web.ProxyModule
import ru.vs.control.server.feature.proxy.web.ProxyModuleImpl
import ru.vs.core.di.Modules
import ru.vs.core.di.i

fun Modules.featureProxy() = DI.Module("feature-proxy") {
    bindSingleton<ProxyConfigurationInteractor> { ProxyConfigurationInteractorImpl() }
    bindSingleton<ProxyModule> { ProxyModuleImpl(i(), i()) }
}