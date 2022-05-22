package ru.vs.control.server.feature.proxy.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal interface ProxyConfigurationInteractor {
    fun observeProxiedHosts(): Flow<Map<String, String>>
}

internal class ProxyConfigurationInteractorImpl : ProxyConfigurationInteractor {
    override fun observeProxiedHosts(): Flow<Map<String, String>> {
        return flowOf(
            mapOf(
                "pve.control.vs" to "https://pve1.test.vs:8006",
                "zm.control.vs" to "https://zoneminder.test.vs",
            )
        )
    }
}