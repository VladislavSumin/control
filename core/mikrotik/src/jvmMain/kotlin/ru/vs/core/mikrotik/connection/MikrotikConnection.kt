package ru.vs.core.mikrotik.connection

import io.ktor.network.sockets.*
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import ru.vs.core.mikrotik.MikrotikTrapException
import ru.vs.core.mikrotik.dsl.MikrotikDsl
import ru.vs.core.mikrotik.message.ClientMessage
import ru.vs.core.mikrotik.message.ServerMessage
import ru.vs.core.mikrotik.message.fromString
import ru.vs.core.serialization.toJsonElement
import kotlin.reflect.KType
import kotlin.reflect.typeOf


interface MikrotikConnection {
    val dsl: MikrotikDsl
    suspend fun executeRaw(command: String): List<Map<String, String>>
    suspend fun executeUnit(command: String)
    suspend fun <T : Any> execute(command: String, type: KType): List<T>
}

internal interface MikrotikConnectionInternal : MikrotikConnection {
    suspend fun executeRaw(message: ClientMessage): List<Map<String, String>>
    suspend fun executeUnit(message: ClientMessage)
    suspend fun <T : Any> execute(message: ClientMessage, type: KType): List<T>
    suspend fun login(username: String, password: String)
}

suspend inline fun <reified T : Any> MikrotikConnection.execute(command: String): List<T> {
    return execute(command, typeOf<T>())
}

internal suspend inline fun <reified T : Any> MikrotikConnectionInternal.execute(
    message: ClientMessage
): List<T> {
    return execute(message, typeOf<T>())
}

internal class MikrotikConnectionImpl(
    socket: Socket,
    private val json: Json
) : MikrotikConnectionInternal {

    private val input = MessageReader(socket.openReadChannel())
    private val output = MessageWriter(socket.openWriteChannel())

    override val dsl: MikrotikDsl = MikrotikDsl()

    override suspend fun login(username: String, password: String) {
        executeRaw(ClientMessage("/login", mapOf("name" to username, "password" to password)))
    }

    override suspend fun executeUnit(command: String) {
        return executeUnit(ClientMessage.fromString(command))
    }

    override suspend fun executeUnit(message: ClientMessage) {
        if (executeRaw(message).isNotEmpty()) throw IllegalStateException("Commend return data")
    }

    override suspend fun <T : Any> execute(command: String, type: KType): List<T> {
        return execute(ClientMessage.fromString(command), type)
    }

    override suspend fun executeRaw(command: String): List<Map<String, String>> {
        return executeRaw(ClientMessage.fromString(command))
    }

    override suspend fun <T : Any> execute(message: ClientMessage, type: KType): List<T> {
        val raw = executeRaw(message)
        val serializer = json.serializersModule.serializer(type)
        return raw.map {
            @Suppress("UNCHECKED_CAST")
            json.decodeFromJsonElement(serializer, it.toJsonElement()) as T
        }
    }

    override suspend fun executeRaw(message: ClientMessage): List<Map<String, String>> {
        output.writeClientMessage(message)
        return input.asFlow()
            .takeWhile { it.result != ServerMessage.Result.done }
            // TODO add custom exceptions
            .onEach { if (it.result == ServerMessage.Result.fatal) throw RuntimeException("server return fatal") }
            .onEach { if (it.result == ServerMessage.Result.trap) throw MikrotikTrapException(it.data["message"]!!) }
            .map { it.data }
            .toList()
    }
}