package ru.vs.core.ktor_client

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.websocket.*

internal interface KtorClientFactory {
    fun createDefault(): HttpClient
}

class KtorClientFactoryImpl(
    private val ktorClientSSLConfigurationInteractor: KtorClientSSLConfigurationInteractor,
) : KtorClientFactory {
    override fun createDefault(): HttpClient {
        return HttpClient(OkHttp) {
            engine {
                config {
                    ktorClientSSLConfigurationInteractor.getSslConfiguration()
                        ?.let { (sslContext, trustManager) ->
                            sslSocketFactory(sslContext.socketFactory, trustManager)
                        }
                }
            }

            install(WebSockets)
        }
    }
}
