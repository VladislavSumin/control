package ru.vs.core.acme.model

import kotlinx.serialization.Serializable

@Serializable
data class AcmeNewAccountRequest(
    val termsOfServiceAgreed: Boolean,
    val contact: List<String>,
)