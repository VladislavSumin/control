package ru.vs.core.mikrotik.connection

import io.ktor.utils.io.*
import ru.vs.core.mikrotik.ClientMessage

internal class MessageWriter(
    private val output: ByteWriteChannel
) {

    suspend fun writeClientMessage(message: ClientMessage) {
        writeWord(message.command)

        message.params.forEach { (k, v) -> writeWord("=$k=$v") }
        message.attributes.forEach { (k, v) -> writeWord(".$k=$v") }
        message.params.forEach { (k, v) -> writeWord("?$k=$v") }

        output.writeByte(0x00)

        output.flush()
    }

    private suspend fun writeWord(word: String) {
        val bytes = word.toByteArray(charset("UTF-8"))
        writeLength(bytes.size)
        output.writeFully(bytes)
    }

    private suspend fun writeLength(length: Int) {
        var len = length
        if (len < 0x80) {
            output.writeByte(len)
        } else if (len < 0x4000) {
            len = len or 0x8000
            output.writeByte(len shr 8)
            output.writeByte(len)
        } else if (len < 0x20000) {
            len = len or 0xC00000
            output.writeByte(len shr 16)
            output.writeByte(len shr 8)
            output.writeByte(len)
        } else if (len < 0x10000000) {
            len = len or -0x20000000
            output.writeByte(len shr 24)
            output.writeByte(len shr 16)
            output.writeByte(len shr 8)
            output.writeByte(len)
        } else {
            output.writeByte(0xF0)
            output.writeByte(len shr 24)
            output.writeByte(len shr 16)
            output.writeByte(len shr 8)
            output.writeByte(len)
        }
    }
}