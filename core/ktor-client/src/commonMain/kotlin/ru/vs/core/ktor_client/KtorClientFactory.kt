package ru.vs.core.ktor_client

import io.ktor.client.*

internal interface KtorClientFactory {
    fun createDefault(): HttpClient
}

expect class KtorClientFactoryImpl() : KtorClientFactory
