package ru.vs.core.mikrotik.connection

import io.ktor.network.sockets.*
import ru.vs.core.mikrotik.message.ClientMessage


interface MikrotikConnection {
}

internal class MikrotikConnectionImpl(
    private val socket: Socket
) : MikrotikConnection {

    val input = MessageReader(socket.openReadChannel())
    val output = MessageWriter(socket.openWriteChannel())

    suspend fun login(username: String, password: String) {
        execute(ClientMessage("/login", mapOf("name" to username, "password" to password)))
    }

    private suspend fun execute(message: ClientMessage) {
        output.writeClientMessage(message)
        println(input.readServerMessage())
    }
}