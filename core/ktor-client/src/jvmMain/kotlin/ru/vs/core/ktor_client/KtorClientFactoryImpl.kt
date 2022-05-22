package ru.vs.core.ktor_client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*

actual class KtorClientFactoryImpl : KtorClientFactory {
    override fun createDefault(): HttpClient {
        return HttpClient(OkHttp)
    }
}
