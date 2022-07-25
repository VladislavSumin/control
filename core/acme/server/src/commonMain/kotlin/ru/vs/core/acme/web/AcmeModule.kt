package ru.vs.core.acme.web

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.vs.core.acme.domain.AcmeServer
import ru.vs.core.acme.model.AcmeDirectory
import ru.vs.core.acme.model.AcmeProtectedRequest
import ru.vs.core.acme.model.createDefault
import java.net.URL

fun Application.acme(
    serverUrl: String,
    basePath: String = "acme",
    acmeServer: AcmeServer
) {
    install(ContentNegotiation) {
        json()
        json(contentType = ContentType("application", "jose+json"))
    }

    // TODO проверять serverUrl

    routing {
        get(basePath) {
            val directory = AcmeDirectory.createDefault(URL("$serverUrl/$basePath"))
            call.respond(directory)
        }
        head("${basePath}/newNonce") {
            call.response.header("Replay-Nonce", acmeServer.newNonce().encodedNonce)
            call.respond(HttpStatusCode.NoContent)
        }
        post("${basePath}/newAccount") {
            val request = call.receive<AcmeProtectedRequest>()
            acmeServer.newAccount(request)
        }
    }
}
