package ru.vs.core.acme.domain

import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.util.Base64URL
import ru.vs.core.acme.model.AcmeProtectedRequest

fun AcmeProtectedRequest.toJWS(): JWSObject {
    return JWSObject(
        Base64URL(protected),
        Base64URL(payload),
        Base64URL(signature),
    )
}