package ru.vs.control.server.feature.proxy.web

import co.touchlab.kermit.Logger
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.*
import io.ktor.util.pipeline.*
import io.ktor.utils.io.*
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
        val pipelineContext = this

        try {
            val response = runCatching {
                httpClient.request("$url${call.request.uri}") {
                    method = pipelineContext.call.request.httpMethod
                    setBody(ProxiedOutgoingContent(call.request))
                }
            }
                .onFailure { logger.w(it) { "Error while executing request" } }
                .getOrThrow()


            val proxiedHeaders = response.headers
            val contentType = proxiedHeaders[HttpHeaders.ContentType]
            val contentLength = proxiedHeaders[HttpHeaders.ContentLength]


            try {
                call.respond(object : OutgoingContent.WriteChannelContent() {
                    override val contentLength: Long? = contentLength?.toLong()
                    override val contentType: ContentType? =
                        contentType?.let { ContentType.parse(it) }
                    override val headers: Headers = Headers.build {
                        appendAll(proxiedHeaders.filter { key, _ ->
                            !key.equals(HttpHeaders.ContentType, ignoreCase = true)
                                    && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
                                    && !key.equals(HttpHeaders.TransferEncoding, ignoreCase = true)
                        })
                    }
                    override val status: HttpStatusCode? = response.status
                    override suspend fun writeTo(channel: ByteWriteChannel) {
                        response.bodyAsChannel().copyAndClose(channel)
                        channel.close()
                    }
                })
            } catch (e: Exception) {
                println("AAAAAA + $e")
                println(proxiedHeaders)
            }
        } catch (e: RedirectResponseException) {
            //TODO тут нужно урл проверять
//                        call.respondRedirect(
//                            // смапил аки царь
//                            e.response.headers.toMap()
//                                .filter { it.key.contentEquals("Location", ignoreCase = true) }
//                                .toList().first()!!.second[0]
//                        )
            println(e)
        } catch (e: ClientRequestException) {
            println("bbbbbbbbb")
            call.respond(e.response.status)
        } catch (e: Exception) {
            println("aaa: $e")
            throw RuntimeException(e)
        }
    }
}