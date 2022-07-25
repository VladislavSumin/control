package ru.vs.core.acme.domain

import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.vs.core.acme.model.AcmeAuthHeader
import ru.vs.core.acme.model.AcmeAuthHeaderAlgorithm
import ru.vs.core.acme.model.AcmeNonce
import ru.vs.core.acme.model.AcmeProtectedRequest

interface AcmeServer {
    suspend fun newNonce(): AcmeNonce
    suspend fun newAccount(request: AcmeProtectedRequest)
}

class AcmeServerImpl(
    private val nonceFactory: AcmeNonceFactory = AcmeNonceFactoryImpl(),
    private val json: Json = Json
) : AcmeServer {
    override suspend fun newNonce(): AcmeNonce {
        return nonceFactory.create()
    }

    // TODO оптимизировать код
    override suspend fun newAccount(request: AcmeProtectedRequest) {
        val jws = request.toJWS()
        val header = json.decodeFromString<AcmeAuthHeader>(jws.header.toString())
        val jwk = JWK.parse(json.encodeToString(header.jwk))

        val verifier: JWSVerifier = when (header.alg) {
            AcmeAuthHeaderAlgorithm.RS256 -> {
                val key = jwk.toRSAKey().toRSAPublicKey()
                if (key.modulus.bitLength() < 2048) throw RuntimeException("Key length to small")
                RSASSAVerifier(key)
            }
        }

        if (!jws.verify(verifier)) throw RuntimeException("Invalid JWS Signature")

        println(header)
    }
}
