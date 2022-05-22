package ru.vs.control.server.feature.proxy.web

import co.touchlab.kermit.Logger
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.request.*
import io.ktor.util.*
import io.ktor.utils.io.*

class ProxiedOutgoingContent(
    private val request: ApplicationRequest
) : OutgoingContent.WriteChannelContent() {
    override val headers: Headers = HeadersBuilder().run {
        appendAll(request.headers.filterContentTypeAndLength())
        build()
    }

    override val contentType: ContentType
        get() = request.contentType()

    override val contentLength: Long? =
        // тут нужно смотреть мб по типу запроса?
        if (request.receiveChannel().isClosedForRead) 0 else request.headers["content-length"]?.toLong()

    override suspend fun writeTo(channel: ByteWriteChannel) {
        try {
            val i = request.receiveChannel().copyAndClose(channel)
            logger.v { "Written $i, content-length $contentLength" }
            channel.flush()
            channel.close()
        } catch (e: Exception) {
            logger.w(e) { "Error on body write" }
            throw e
        }
    }

    companion object {
        private val logger = Logger.withTag("proxy-ProxiedOutgoingContent")
    }
}

private fun Headers.filterContentTypeAndLength() = filter { key, _ ->
    !key.equals(HttpHeaders.ContentType, ignoreCase = true)
            && !key.equals(HttpHeaders.ContentLength, ignoreCase = true)
}
