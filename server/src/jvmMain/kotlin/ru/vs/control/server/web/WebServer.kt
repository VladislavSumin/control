package ru.vs.control.server.web

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import ru.vs.control.server.feature.proxy.web.ProxyModule

private const val SERVER_DEFAULT_PORT = 8080

interface WebServer {
    suspend fun run()
}

class WebServerImpl(
    private val proxyModule: ProxyModule,
) : WebServer {
    override suspend fun run() {
        withContext(CoroutineName("web-server")) {
            val environment = createEnvironment()
            val server = createEmbeddedServer(environment)
            server.start(true)
        }
    }

    //
//    private fun getTrustManagerFactory(): TrustManagerFactory? {
//        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
////        trustManagerFactory.init(null)
//        return trustManagerFactory
//    }
//
//    private fun getSslContext(): SSLContext? {
//        val sslContext = SSLContext.getInstance("TLS")
//        sslContext.init(null, arrayOf(getTrustManager()), null)
//        return sslContext
//    }
//
//    private fun getTrustManager(): X509TrustManager {
//        return object : X509TrustManager {
//            override fun getAcceptedIssuers(): Array<X509Certificate> {
//                return emptyArray()
//            }
//
//            override fun checkClientTrusted(
//                certs: Array<X509Certificate>, authType: String
//            ) {
//            }
//
//            override fun checkServerTrusted(
//                certs: Array<X509Certificate>, authType: String
//            ) {
//            }
//        }
//    }
//
    private fun CoroutineScope.createEnvironment(): ApplicationEngineEnvironment {
        return applicationEngineEnvironment {
            // Append parent coroutine context
            parentCoroutineContext = coroutineContext

            connector {
                host = "0.0.0.0"
                port = SERVER_DEFAULT_PORT
            }

            proxyModule.apply { install() }

//            module {
////                // Creates a new HttpClient
////                val client = HttpClient(OkHttp) {
////                    engine {
////                        config {
////                            sslSocketFactory(getSslContext()!!.socketFactory, getTrustManager())
////                        }
////                    }
////                }
//
//                // Creates a new HttpClient
//                val client = HttpClient(Java) {
//                    this.followRedirects = false
//                    engine {
//                        this.config {
//
//                        }
//                        config {
//                            sslContext(getSslContext()!!)
//                        }
//                    }
//                }
//
//                intercept(ApplicationCallPipeline.Call) {
//                    val context = this
//
//                    if (context.call.request.uri.contains("ticket")) {
//                        println("a")
//                    }
//
//                    if (context.call.request.headers["Connection"] != "keep-alive") {
//                        println("b")
//                    }
//
//                    if (context.call.request.headers["Upgrade"] != null) {
//                        println("Upgrade = ${context.call.request.headers["Upgrade"]}")
//                    }
//                    call.request.httpVersion
//
//                    try {
////                    val response =
//                        client.request<HttpStatement>("https://pve1.test.vs:8006${call.request.uri}") {
////                        client.request<HttpStatement>("https://zoneminder.test.vs${call.request.uri}") {
//                            method = context.call.request.httpMethod
//
//                            headers {
//                                this.appendAll(
//                                    context.call.request.headers
//                                        .filter { key, _ ->
//                                            !key.equals(
//                                                HttpHeaders.ContentType,
//                                                ignoreCase = true
//                                            ) && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
//                                        }
//
//                                )
//                            }
//                            this.body = object : OutgoingContent.WriteChannelContent() {
//                                override val contentType: ContentType?
//                                    get() = call.request.contentType()
//
//                                override val contentLength: Long? =
//                                    // тут нужно смотреть мб по типу запроса?
//                                    if (context.call.request.receiveChannel().isClosedForRead) 0 else
//                                        call.request.headers["content-length"]?.toLong()
//
//                                override suspend fun writeTo(channel: ByteWriteChannel) {
//                                    try {
//                                        val i = context.call.request.receiveChannel().copyAndClose(channel)
//                                        println("written $i")
//                                    } catch (e: Exception) {
//                                        println("BBBB")
//                                    }
//                                }
//                            }
//                        }
//                            .execute { response ->
//
//
//                                val proxiedHeaders = response.headers
//                                val contentType = proxiedHeaders[HttpHeaders.ContentType]
//                                val contentLength = proxiedHeaders[HttpHeaders.ContentLength]
//
//
//                                try {
//                                    call.respond(object : OutgoingContent.WriteChannelContent() {
//                                        override val contentLength: Long? = contentLength?.toLong()
//                                        override val contentType: ContentType? =
//                                            contentType?.let { ContentType.parse(it) }
//                                        override val headers: Headers = Headers.build {
//                                            appendAll(proxiedHeaders.filter { key, _ ->
//                                                !key.equals(HttpHeaders.ContentType, ignoreCase = true)
//                                                        && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
//                                                        && !key.equals(HttpHeaders.TransferEncoding, ignoreCase = true)
//                                            })
//                                        }
//                                        override val status: HttpStatusCode? = response.status
//                                        override suspend fun writeTo(channel: ByteWriteChannel) {
//                                            response.content.copyAndClose(channel)
//                                        }
//                                    })
//                                } catch (e: Exception) {
//                                    println("AAAAAA + $e")
//                                    println(proxiedHeaders)
//                                }
//                            }
//                    } catch (e: RedirectResponseException) {
//                        //TODO тут нужно урл проверять
////                        call.respondRedirect(
////                            // смапил аки царь
////                            e.response.headers.toMap()
////                                .filter { it.key.contentEquals("Location", ignoreCase = true) }
////                                .toList().first()!!.second[0]
////                        )
//                        println(e)
//                    } catch (e: ClientRequestException) {
//                        call.respond(e.response.status)
//                    } catch (e: Exception) {
//                        println("aaa")
//                        throw RuntimeException(e)
//                    }
//                }
//            }
        }
    }

    private fun createEmbeddedServer(environment: ApplicationEngineEnvironment): ApplicationEngine =
        embeddedServer(Netty, environment)
}
