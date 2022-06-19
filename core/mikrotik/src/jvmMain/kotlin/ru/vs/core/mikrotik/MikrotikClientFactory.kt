package ru.vs.core.mikrotik

import kotlinx.serialization.json.Json
import ru.vs.core.ktor_network.SocketFactory

internal interface MikrotikClientFactory {
    fun createMikrotikClient(): MikrotikClient
}

internal class MikrotikClientFactoryImpl(
    private val socketFactory: SocketFactory,
    private val json: Json,
) : MikrotikClientFactory {
    override fun createMikrotikClient(): MikrotikClient {
        return MikrotikClientImpl(socketFactory, json)
    }
}