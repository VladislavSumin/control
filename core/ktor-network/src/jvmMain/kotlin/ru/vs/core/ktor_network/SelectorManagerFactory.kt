package ru.vs.core.ktor_network

import io.ktor.network.selector.*
import ru.vs.core.coroutines.DispatchersProvider

internal interface SelectorManagerFactory {
    fun createSelectorManager(): SelectorManager
}

internal class SelectorManagerFactoryImpl(
    private val dispatchersProvider: DispatchersProvider
) : SelectorManagerFactory {
    override fun createSelectorManager(): SelectorManager {
        return SelectorManager(dispatchersProvider.IO)
    }
}