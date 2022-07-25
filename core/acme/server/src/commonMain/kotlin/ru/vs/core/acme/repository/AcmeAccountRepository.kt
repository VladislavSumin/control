package ru.vs.core.acme.repository

import ru.vs.core.acme.model.AcmeAccountId

interface AcmeAccountRepository {
    suspend fun add(acmeAccount: AcmeAccount): AcmeAccount
    suspend fun find(id: AcmeAccountId): AcmeAccount?
}