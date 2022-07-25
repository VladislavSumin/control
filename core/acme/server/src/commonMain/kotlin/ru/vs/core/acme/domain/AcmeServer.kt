package ru.vs.core.acme.domain

import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.JWK
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.vs.core.acme.model.*
import ru.vs.core.acme.repository.AcmeNonceRepository
import ru.vs.core.acme.repository.AcmeNonceRepositoryImpl

interface AcmeServer {
    suspend fun newNonce(): AcmeNonce
    suspend fun newAccount(request: AcmeProtectedRequest)
}

class AcmeServerImpl(
    private val nonceFactory: AcmeNonceFactory = AcmeNonceFactoryImpl(),
    private val nonceRepository: AcmeNonceRepository = AcmeNonceRepositoryImpl(),
    private val json: Json = Json,
) : AcmeServer {
    override suspend fun newNonce(): AcmeNonce {
        val nonce = nonceFactory.create()
        nonceRepository.saveNonce(nonce)
        return nonce
    }

    // TODO оптимизировать код
    override suspend fun newAccount(request: AcmeProtectedRequest) {
        val jws = request.toJWS()
        val header = json.decodeFromString<AcmeAuthHeader>(jws.header.toString())
        val jwk = JWK.parse(json.encodeToString(header.jwk))

        // verify nonce
        if (nonceRepository.deleteNonce(header.nonce)) throw RuntimeException("Invalid nonce")

        // verify signature
        val verifier: JWSVerifier = when (header.alg) {
            AcmeAuthHeaderAlgorithm.RS256 -> {
                val key = jwk.toRSAKey().toRSAPublicKey()
                if (key.modulus.bitLength() < 2048) throw RuntimeException("Key length to small")
                RSASSAVerifier(key)
            }
        }

        if (!jws.verify(verifier)) throw RuntimeException("Invalid JWS Signature")

        val body = json.decodeFromString<AcmeNewAccountRequest>(jws.payload.toString())

        if (!body.termsOfServiceAgreed) throw RuntimeException("termsOfServiceAgreed is false")

        println(header)
    }
}
