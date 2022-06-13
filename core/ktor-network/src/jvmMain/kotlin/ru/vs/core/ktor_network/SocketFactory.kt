package ru.vs.core.ktor_network

import io.ktor.network.selector.*
import io.ktor.network.sockets.*

interface SocketFactory {
    fun aSocket(): SocketBuilder
}

internal class SocketFactoryImpl(
    private val selectorManager: SelectorManager,
) : SocketFactory {
    override fun aSocket(): SocketBuilder {
        return aSocket(selectorManager)
    }
}
