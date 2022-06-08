package ru.vs.control.server.feature.proxy.web

import co.touchlab.kermit.Logger
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.util.*
import io.ktor.utils.io.*

class ProxiedOutgoingContentResponse(
    private val response: HttpResponse
) : OutgoingContent.WriteChannelContent() {
    override val headers: Headers = HeadersBuilder().run {
        // TODO поискать zero copy решение
        appendAll(response.headers.filterContentTypeAndLength())
        build()
    }

    override val contentType: ContentType? = response.headers[HttpHeaders.ContentType]?.let { ContentType.parse(it) }

    override val contentLength: Long? = response.headers[HttpHeaders.ContentLength]?.toLong()

    override val status: HttpStatusCode = response.status

    override suspend fun writeTo(channel: ByteWriteChannel) {
        try {
            val i = response.bodyAsChannel().copyAndClose(channel)
            logger.v { "Written $i, content-length $contentLength" }
        } catch (e: Exception) {
            logger.w(e) { "Error on body write" }
            throw e
        }
    }

    companion object {
        private val logger = Logger.withTag("proxy-ProxiedOutgoingContentResponse")
    }
}

private fun Headers.filterContentTypeAndLength() = filter { key, _ ->
    !key.equals(HttpHeaders.ContentType, ignoreCase = true)
            && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
            && !key.equals(HttpHeaders.TransferEncoding, ignoreCase = true)
}
