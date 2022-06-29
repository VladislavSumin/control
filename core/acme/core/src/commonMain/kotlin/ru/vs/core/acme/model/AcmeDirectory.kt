package ru.vs.core.acme.model

import kotlinx.serialization.Serializable

@Serializable
data class AcmeDirectory(
    val newNonce: String, //TODO replace by url?
    val newAccount: String,
    val newOrder: String,
    val newAuthz: String,
    val revokeCert: String,
    val keyChange: String,
) {
    companion object
}