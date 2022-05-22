package ru.vs.control.server.feature.proxy

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.server.feature.proxy.web.ProxyModule
import ru.vs.control.server.feature.proxy.web.ProxyModuleImpl
import ru.vs.core.di.Modules

fun Modules.featureProxy() = DI.Module("feature-proxy") {
    bindSingleton<ProxyModule> { ProxyModuleImpl() }
}