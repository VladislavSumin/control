package ru.vs.core.acme.web

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.vs.core.acme.domain.AcmeNonceFactory
import ru.vs.core.acme.domain.AcmeNonceFactoryImpl
import ru.vs.core.acme.model.AcmeDirectory
import ru.vs.core.acme.model.createDefault
import java.net.URL

fun Application.acme(
    serverUrl: String, // https://ca.control.vs:8443
    basePath: String = "acme",
    nonceFactory: AcmeNonceFactory = AcmeNonceFactoryImpl()
) {
    routing {
        get(basePath) {
            val directory = AcmeDirectory.createDefault(URL("$serverUrl/$basePath"))
            call.respond(directory)
        }
        head("${basePath}/newNonce") {
            call.response.header("Replay-Nonce", nonceFactory.create().encodedNonce)
            call.respond(HttpStatusCode.NoContent)
        }
    }
}