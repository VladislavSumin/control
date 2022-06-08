package ru.vs.control.server.feature.proxy.web

import co.touchlab.kermit.Logger
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.ktor.utils.io.*

class ProxiedOutgoingContentRequest(
    private val request: ApplicationRequest
) : OutgoingContent.WriteChannelContent() {
    override val headers: Headers = HeadersBuilder().run {
        // TODO поискать zero copy решение
        appendAll(request.headers.filterContentTypeAndLength())

        // java.io.EOFException: \n not found: limit=0 content=…
        // https://javamana.com/2022/02/202202020336318130.html
        // https://github.com/ktorio/ktor/issues/1708#issuecomment-609988128
        append(HttpHeaders.Connection, "close")

        build()
    }

    override val contentType: ContentType = request.contentType()

    override val contentLength: Long? =
        if (request.receiveChannel().isClosedForRead) 0 else request.headers["content-length"]?.toLong()

    override suspend fun writeTo(channel: ByteWriteChannel) {
        try {
            val i = request.receiveChannel().copyAndClose(channel)
            logger.v { "Written $i, content-length $contentLength" }
        } catch (e: Exception) {
            logger.w(e) { "Error on body write" }
            throw e
        }
    }

    companion object {
        private val logger = Logger.withTag("proxy-ProxiedOutgoingContentRequest")
    }
}

private fun Headers.filterContentTypeAndLength() = filter { key, _ ->
    !key.equals(HttpHeaders.ContentType, ignoreCase = true)
            && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
}
