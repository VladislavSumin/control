package ru.vs.core.mikrotik.connection

import io.ktor.network.sockets.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import ru.vs.core.mikrotik.dsl.MikrotikDsl
import ru.vs.core.mikrotik.message.ClientMessage
import ru.vs.core.mikrotik.message.ServerMessage
import ru.vs.core.mikrotik.message.fromString


interface MikrotikConnection {
    val dsl: MikrotikDsl
    suspend fun execute(command: String): List<Map<String, String>>
}

internal class MikrotikConnectionImpl(
    socket: Socket
) : MikrotikConnection {

    private val input = MessageReader(socket.openReadChannel())
    private val output = MessageWriter(socket.openWriteChannel())

    override val dsl: MikrotikDsl = MikrotikDsl()

    override suspend fun execute(command: String): List<Map<String, String>> {
        return execute(ClientMessage.fromString(command))
    }

    suspend fun login(username: String, password: String) {
        execute(ClientMessage("/login", mapOf("name" to username, "password" to password)))
    }

    private suspend fun execute(message: ClientMessage): List<Map<String, String>> {
        output.writeClientMessage(message)
        return input.asFlow()
            .takeWhile { it.result != ServerMessage.Result.done }
            // TODO add custom exceptions
            .onEach { if (it.result == ServerMessage.Result.fatal) throw RuntimeException("server return fatal") }
            .onEach { if (it.result == ServerMessage.Result.trap) throw RuntimeException("server return trap") }
            .map { it.data }
            .toList()
    }
}