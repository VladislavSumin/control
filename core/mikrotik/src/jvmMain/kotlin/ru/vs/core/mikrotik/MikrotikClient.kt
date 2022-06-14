package ru.vs.core.mikrotik

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext
import ru.vs.core.ktor_network.SocketFactory
import ru.vs.core.mikrotik.connection.MikrotikConnection
import ru.vs.core.mikrotik.connection.MikrotikConnectionImpl

interface MikrotikClient {
    companion object {
        const val DEFAULT_PORT = 8728
    }

    suspend fun connect(
        hostname: String, port: Int = DEFAULT_PORT,
        username: String, password: String,
        action: MikrotikConnection.() -> Unit
    )
}

internal class MikrotikClientImpl(
    private val socketFactory: SocketFactory
) : MikrotikClient {
    override suspend fun connect(
        hostname: String,
        port: Int,
        username: String,
        password: String,
        action: MikrotikConnection.() -> Unit
    ) {
        val name = "$username@$hostname:$port"
        withContext(CoroutineName("MC connection $name")) {
            logger.d { "Connecting to $name" }
            socketFactory.aSocket().tcp().connect(hostname, port).use { socket ->
                logger.v { "Connection with $name established" }
                val clientConnection = MikrotikConnectionImpl(socket)
                clientConnection.login(username, password)
                clientConnection.action()
            }
        }
    }

    companion object {
        val logger = Logger.withTag("MikrotikClient")
    }
}