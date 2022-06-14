package ru.vs.core.mikrotik.connection

import io.ktor.utils.io.*


internal class MessageReader(
    private val input: ByteReadChannel
) {

    suspend fun readWord(): String {
        val len = readLength()
        //TODO allocate once
        val data = ByteArray(len)
        input.readFully(data)
        return String(data, charset("UTF-8"))
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