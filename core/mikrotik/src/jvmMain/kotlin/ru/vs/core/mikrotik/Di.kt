package ru.vs.core.mikrotik

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.core.di.Modules
import ru.vs.core.di.i

fun Modules.coreMikrotik() = DI.Module("core-mikrotik") {
    bindSingleton<MikrotikClientFactory> { MikrotikClientFactoryImpl(i(), i()) }
    bindSingleton { i<MikrotikClientFactory>().createMikrotikClient() }
}