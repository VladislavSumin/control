package ru.vs.core.acme.model

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class AcmeNonce(val encodedNonce: String){
    companion object
}