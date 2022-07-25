package ru.vs.core.acme.model

import kotlinx.serialization.Serializable

@Serializable
data class AcmeAuthHeader(
    val alg: String,
    val jwk: AcmeAuthJwk,
    val nonce: AcmeNonce,
    val url: String,
)
