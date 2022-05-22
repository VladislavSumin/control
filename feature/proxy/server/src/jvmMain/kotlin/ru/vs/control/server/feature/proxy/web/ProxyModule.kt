package ru.vs.control.server.feature.proxy.web

import io.ktor.server.engine.*

interface ProxyModule {
    fun ApplicationEngineEnvironmentBuilder.install()
}

internal class ProxyModuleImpl : ProxyModule {
    override fun ApplicationEngineEnvironmentBuilder.install() {

    }
}
