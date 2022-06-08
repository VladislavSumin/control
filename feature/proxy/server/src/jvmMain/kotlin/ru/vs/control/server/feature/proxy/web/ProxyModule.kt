package ru.vs.control.server.feature.proxy.web

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import kotlinx.coroutines.launch
import ru.vs.control.server.feature.proxy.domain.ProxyConfigurationInteractor

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

            var proxiedHosts: Map<String, String> = emptyMap()
            launch {
                proxyConfigurationInteractor.observeProxiedHosts().collect {
                    logger.d { "Configuration changed to $it" }
                    proxiedHosts = it
                }
            }

            intercept(ApplicationCallPipeline.Call) {
                logger.v {
                    val request = call.request
                    "Incoming ${request.httpMethod.value} request: ${request.host()}${request.uri}"
                }
                val requestedHost = call.request.host()
                val redirectUrl = proxiedHosts[requestedHost]

                if (redirectUrl != null) proxy(redirectUrl)
                else call.respondText("No proxy for host $requestedHost")
            }
        }
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.proxy(url: String) {
        val response = sendRequestToDestinationHost(url)
            .onFailure { logger.w(it) { "Error while executing request" } }
            .getOrThrow()

        runCatching {
            call.respond(ProxiedOutgoingContentResponse(response))
        }
            .onFailure { logger.w(it) { "Error while executing response" } }
            .getOrThrow()
    }

    private suspend fun PipelineContext<Unit, ApplicationCall>.sendRequestToDestinationHost(url: String): Result<HttpResponse> {
        return runCatching {
            httpClient.request("$url${call.request.uri}") {
                method = call.request.httpMethod
                setBody(ProxiedOutgoingContentRequest(call.request))
            }
        }
    }
}