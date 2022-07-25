package ru.vs.core.acme.model

import kotlinx.serialization.Serializable

@Serializable
data class AcmeProtectedRequest(
    val protected: String,
    val payload: String,
    val signature: String,
)