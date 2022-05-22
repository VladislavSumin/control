package ru.vs.control.server.feature.proxy.web

import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
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
import io.ktor.utils.io.*
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

interface ProxyModule {
    fun ApplicationEngineEnvironmentBuilder.install()
}

internal class ProxyModuleImpl : ProxyModule {
    override fun ApplicationEngineEnvironmentBuilder.install() {
        module {
            // Creates a new HttpClient
            val client = HttpClient(OkHttp) {
                engine {
                    config {
                        sslSocketFactory(getSslContext()!!.socketFactory, getTrustManager())
                    }
                }
            }

            intercept(ApplicationCallPipeline.Call) {
                val context = this

                if (context.call.request.uri.contains("ticket")) {
                    println("a")
                }

                if (context.call.request.headers["Connection"] != "keep-alive") {
                    println("b")
                }

                if (context.call.request.headers["Upgrade"] != null) {
                    println("Upgrade = ${context.call.request.headers["Upgrade"]}")
                }
                call.request.httpVersion

                try {
                    val response =
                        client.request("https://pve1.test.vs:8006${call.request.uri}") {
//                        client.request<HttpStatement>("https://zoneminder.test.vs${call.request.uri}") {
                            method = context.call.request.httpMethod

                            headers {
                                this.appendAll(
                                    context.call.request.headers
                                        .filter { key, _ ->
                                            !key.equals(
                                                HttpHeaders.ContentType,
                                                ignoreCase = true
                                            ) && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
                                        }

                                )
                            }
                            setBody(object : OutgoingContent.WriteChannelContent() {
                                override val contentType: ContentType?
                                    get() = call.request.contentType()

                                override val contentLength: Long? =
                                    // тут нужно смотреть мб по типу запроса?
                                    if (context.call.request.receiveChannel().isClosedForRead) 0 else
                                        call.request.headers["content-length"]?.toLong()

                                override suspend fun writeTo(channel: ByteWriteChannel) {
                                    try {
                                        val i = context.call.request.receiveChannel().copyAndClose(channel)
                                        channel.close()
                                        println("written $i / content len $contentLength")
                                    } catch (e: Exception) {
                                        println("BBBB")
                                    }
                                }
                            })
                        }


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
                    call.respond(e.response.status)
                } catch (e: Exception) {
                    println("aaa")
                    throw RuntimeException(e)
                }
            }
        }
    }


    private fun getTrustManagerFactory(): TrustManagerFactory? {
        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
//        trustManagerFactory.init(null)
        return trustManagerFactory
    }

    private fun getSslContext(): SSLContext? {
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(getTrustManager()), null)
        return sslContext
    }

    private fun getTrustManager(): X509TrustManager {
        return object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return emptyArray()
            }

            override fun checkClientTrusted(
                certs: Array<X509Certificate>, authType: String
            ) {
            }

            override fun checkServerTrusted(
                certs: Array<X509Certificate>, authType: String
            ) {
            }
        }
    }

}
