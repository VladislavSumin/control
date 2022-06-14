package ru.vs.core.mikrotik

import ru.vs.core.ktor_network.SocketFactory

internal interface MikrotikClientFactory {
    fun createMikrotikClient(): MikrotikClient
}

internal class MikrotikClientFactoryImpl(
    private val socketFactory: SocketFactory
) : MikrotikClientFactory {
    override fun createMikrotikClient(): MikrotikClient {
        return MikrotikClientImpl(socketFactory)
    }
}