package ru.vs.core.acme.repository

import ru.vs.core.acme.model.AcmeAccountId
import ru.vs.core.acme.model.AcmeAuthSignatureAlgorithm

data class AcmeAccount(
    val id: AcmeAccountId? = null,
    val alg: AcmeAuthSignatureAlgorithm,
    val jwk: String,
    val contacts: List<String>,
)
