package ru.vs.core.acme.model

import kotlinx.serialization.Serializable

@Serializable
data class AcmeAuthJwk(
    val kty: String,
    val e: String,
    val n: String,
)
