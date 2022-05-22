package ru.vs.core.ktor_client

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.core.di.Modules
import ru.vs.core.di.i

fun Modules.coreKtorClient() = DI.Module("core-ktor-client") {
    bindSingleton<KtorClientFactory> { KtorClientFactoryImpl(i()) }
    bindSingleton { i<KtorClientFactory>().createDefault() }
}