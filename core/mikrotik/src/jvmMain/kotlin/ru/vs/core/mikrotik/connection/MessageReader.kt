package ru.vs.core.mikrotik.connection

import io.ktor.util.*
import io.ktor.utils.io.*
import ru.vs.core.mikrotik.message.ServerMessage
import java.nio.ByteBuffer

private const val BUFFER_SIZE = 512

internal class MessageReader(
    private val input: ByteReadChannel
) {

    private val buffer = ByteBuffer.allocate(BUFFER_SIZE)
    suspend fun readServerMessage(): ServerMessage {
        val rawResult = readWord().substring(1)
        val result = ServerMessage.Result.values().first { it.name == rawResult }

        val data = buildMap {
            while (true) {
                val word = readWord()
                if (word.isEmpty()) break
                val (key, value) = word.substring(1).split("=")
                this[key] = value
            }
        }

        return ServerMessage(result, data)
    }

    private suspend fun readWord(): String {
        val len = readLength()
        if (len > buffer.capacity()) throw RuntimeException("Message size $len greater than buffer size ${buffer.capacity()}")
        buffer.position(0)
        buffer.limit(len)
        input.readFully(buffer)
        buffer.flip()
        return buffer.decodeString(charset("UTF-8"))
    }

    private suspend fun readLength(): Int {
        var len: Int = input.readByte().toInt()
        if (len < 0) throw RuntimeException("Wrong word length $len")
        if (len and 0x80 == 0) {
            // no action
        } else if (len and 0xC0 == 0x80) {
            len = len and 0xC0.inv()
            len = len shl 8 or input.readByte().toInt()
        } else if (len and 0xE0 == 0xC0) {
            len = len and 0xE0.inv()
            len = len shl 8 or input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
        } else if (len and 0xF0 == 0xE0) {
            len = len and 0xF0.inv()
            len = len shl 8 or input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
        } else if (len and 0xF8 == 0xF0) {
            len = input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
            len = len shl 8 or input.readByte().toInt()
        }
        return len
    }
}