package ru.vs.core.mikrotik.connection

import io.ktor.network.sockets.*
import ru.vs.core.mikrotik.message.ClientMessage
import ru.vs.core.mikrotik.message.fromString


interface MikrotikConnection {
    suspend fun execute(command: String)
}

internal class MikrotikConnectionImpl(
    socket: Socket
) : MikrotikConnection {

    private val input = MessageReader(socket.openReadChannel())
    private val output = MessageWriter(socket.openWriteChannel())

    override suspend fun execute(command: String) {
        execute(ClientMessage.fromString(command))
    }

    suspend fun login(username: String, password: String) {
        execute(ClientMessage("/login", mapOf("name" to username, "password" to password)))
    }

    private suspend fun execute(message: ClientMessage) {
        output.writeClientMessage(message)
        println(input.readServerMessage()) // TODO add normal read
    }
}