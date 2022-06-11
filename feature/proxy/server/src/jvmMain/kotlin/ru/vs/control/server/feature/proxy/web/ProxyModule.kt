package ru.vs.control.server.feature.proxy.web

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.websocket.*
import io.ktor.server.websocket.WebSockets
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import ru.vs.control.server.feature.proxy.domain.ProxyConfigurationInteractor
import java.io.EOFException

interface ProxyModule {
    fun ApplicationEngineEnvironmentBuilder.install()
}

internal class ProxyModuleImpl(
    private val proxyConfigurationInteractor: ProxyConfigurationInteractor,
    defaultHttpClient: HttpClient
) : ProxyModule {
    private val httpClient = defaultHttpClient.config {
        followRedirects = false
    }

    private val logger = Logger.withTag("Proxy")

    override fun ApplicationEngineEnvironmentBuilder.install() {
        module {

            install(WebSockets)

            var proxiedHosts: Map<String, String> = emptyMap()
            launch {
                proxyConfigurationInteractor.observeProxiedHosts().collect {
                    logger.d { "Configuration changed to $it" }
                    proxiedHosts = it
                }
            }

            intercept(ApplicationCallPipeline.Call) {
                val request = call.request
                runCatching {
                    logger.v { "Incoming ${request.httpMethod.value} request: ${request.host()}${request.uri}" }
                    val requestedHost = request.host()
                    val redirectUrl = proxiedHosts[requestedHost]

                    if (redirectUrl != null) proxy(redirectUrl)
                    else call.respondText("No proxy for host $requestedHost")

                    finish()
                }
                    .onFailure {
                        logger.w(it) { "Error on process ${request.httpMethod.value} request: ${request.host()}${request.uri}" }
                        call.respond(HttpStatusCode.InternalServerError)
                    }
            }
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.proxy(url: String) {
        if (call.request.headers[HttpHeaders.Connection]!!.equals("Upgrade", false)) {
            proxyWebsocket(url)
        } else {
            val response = sendRequestToDestinationHost(url)
            call.respond(ProxiedOutgoingContentResponse(response))
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.proxyWebsocket(url: String) {
        val requestHeaders = call.request.headers

        if (!"websocket".equals(requestHeaders[HttpHeaders.Upgrade], false))
            throw RuntimeException("Only websocket supported, but ${requestHeaders[HttpHeaders.Upgrade]} received")

        val protocol =
            requestHeaders[HttpHeaders.SecWebSocketProtocol] ?: throw RuntimeException("websocket protocol must be set")

        call.respondWebSocketRaw(protocol = protocol) {
            val clientSideSession = this
            runCatching {
                httpClient.webSocket(
                    "$url${call.request.uri}"
                        .replace("https", "wss")
                        .replace("http", "ws"),
                    {
                        headers.appendAll(
                            call.request.headers.filter { key, _ ->
                                !key.equals(HttpHeaders.Upgrade, false)
                                        && !key.equals(HttpHeaders.SecWebSocketExtensions, false)
                            }
                        )
                    }
                ) {
                    val serverSideSession = this

                    coroutineScope {

                        launch {
                            serverSideSession.incoming.copyAndClose(clientSideSession.outgoing)
                        }

                        launch {
                            clientSideSession.incoming.copyAndClose(serverSideSession.outgoing)
                        }
                    }
                }
            }
                .onFailure {
                    logger.w("Error on ws", it)
                }
        }
    }


    private suspend fun PipelineContext<Unit, ApplicationCall>.sendRequestToDestinationHost(url: String): HttpResponse {
        return httpClient.request("$url${call.request.uri}") {
            method = call.request.httpMethod
            setBody(ProxiedOutgoingContentRequest(call.request))
        }
    }

    /**
     * Copied from io.ktor.server.websocket.RoutingKt.respondWebSocketRaw
     */
    private suspend fun ApplicationCall.respondWebSocketRaw(
        protocol: String? = null,
        negotiateExtensions: Boolean = false,
        handler: suspend WebSocketSession.() -> Unit
    ) {
        respond(WebSocketUpgrade(this, protocol, negotiateExtensions, handler))
    }
}

private suspend fun ReceiveChannel<Frame>.copyAndClose(channel: SendChannel<Frame>) {
    try {
        for (frame in this) {
            channel.send(frame)
        }
    } catch (e: EOFException) {
        // no action
    } finally {
        channel.close()
    }
}
