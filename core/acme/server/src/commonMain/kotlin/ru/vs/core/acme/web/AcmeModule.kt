package ru.vs.core.acme.web

import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.ECDSAVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import ru.vs.core.acme.domain.AcmeServer
import ru.vs.core.acme.domain.AcmeServerImpl
import ru.vs.core.acme.domain.toJWS
import ru.vs.core.acme.model.AcmeAuthHeader
import ru.vs.core.acme.model.AcmeDirectory
import ru.vs.core.acme.model.AcmeProtectedRequest
import ru.vs.core.acme.model.createDefault
import java.net.URL
import java.security.KeyFactory

fun Application.acme(
    serverUrl: String,
    basePath: String = "acme",
    acmeServer: AcmeServer = AcmeServerImpl()
) {
    install(ContentNegotiation) {
        json()
        json(contentType = ContentType("application", "jose+json"))
    }

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
            val jws = request.toJWS()
            val header = Json.decodeFromString<AcmeAuthHeader>(jws.header.toString())
            println(header)
        }
    }
}

fun test() {
    val a = JWK.parse(
        "      {  \"n\": \"qJNA7nWNrtQ0BgvoO440S-JrOA4tCKXW8IJUQk_QRF-Aq16DOfqsMm21_83gw_tDKcr5TMPz4H8nxHac36jFVz-mFk6Ax6HDW5xtaVfAOOOPCZ2Y0ebB4uEuiBRFvFHNfwTEx0oSeQsWj7_fTUMTp1UE8dzYN9mP7ylAz3pdDXLE7kxbwsxUK0nRG38BJu64mXvsEOqPN2-XYfHGCJMDffUUYvcszdF7h7R42RwuIvfEZL9Ued4DtKlJSJJnlTFILeu5dQP5OeZnl5-5juvQ_T5cyhH4W5D7pody59EgWgFi7IOBxzur2lYkhQ2UPz5Cr3gMrQMka1ff60Djd-NFTQ\",\n" +
                "        \"e\": \"AQAB\",\n" +
                "        \"kty\": \"RSA\"}"
    )
    println(a)

    val b = JWSObject.parse(
        "eyJhbGciOiAiUlMyNTYiLCAiandrIjogeyJuIjogInFKTkE3bldOcnRRMEJndm9PNDQwUy1Kck9BNHRDS1hXOElKVVFrX1FSRi1BcTE2RE9mcXNNbTIxXzgzZ3dfdERLY3I1VE1QejRIOG54SGFjMzZqRlZ6LW1GazZBeDZIRFc1eHRhVmZBT09PUENaMlkwZWJCNHVFdWlCUkZ2RkhOZndURXgwb1NlUXNXajdfZlRVTVRwMVVFOGR6WU45bVA3eWxBejNwZERYTEU3a3hid3N4VUswblJHMzhCSnU2NG1YdnNFT3FQTjItWFlmSEdDSk1EZmZVVVl2Y3N6ZEY3aDdSNDJSd3VJdmZFWkw5VWVkNER0S2xKU0pKbmxURklMZXU1ZFFQNU9lWm5sNS01anV2UV9UNWN5aEg0VzVEN3BvZHk1OUVnV2dGaTdJT0J4enVyMmxZa2hRMlVQejVDcjNnTXJRTWthMWZmNjBEamQtTkZUUSIsICJlIjogIkFRQUIiLCAia3R5IjogIlJTQSJ9LCAibm9uY2UiOiAiQUFBQmdiRFNVSUNqR3pKVHNmQ2hfa1oxUk9keDhtOHZtSWRnb2RwT3B1TSIsICJ1cmwiOiAiaHR0cHM6Ly9jYS5jb250cm9sLnZzOjg0NDMvYWNtZS9uZXdBY2NvdW50In0" +
                ".ewogICJjb250YWN0IjogWwogICAgIm1haWx0bzphQGEuYSIKICBdLAogICJ0ZXJtc09mU2VydmljZUFncmVlZCI6IHRydWUKfQ" +
                ".jtu38Eug1Io-uGyvUHpOT-Qe0KgdE8jLqlGtKu1pCxWjfYIyMM6DEgs1DvOPAN6FTeGtIWjAIPQ-VMwAoVvnaiB7-lxgPOc3ykPGUPMImZgy1-ggPrWLQObNmVKBmOov05t5xQdpcBn6c8vShfCPcWCFNsuylgI8mJFMXKzNyPlhYGNZZSbY75EblvslqGc8V1-JmfYpsBDTP74i3-LcYs1tuZrPVpTyGpnADJUtTmZpXxyajc9VTEQU1wKKmvqcH4xyQGRQI7_qcuY32--sMyACgb5olPvrAPk2P9Oa2N_BeAlwn5BKO4z5XfGYtwxqU0iBw7cCx66eACyhHygW9w"
    )

    println(b.verify(RSASSAVerifier(a.toRSAKey())))

    println(b.header)
    println(b.payload)
    println(b)
}
