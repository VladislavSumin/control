package ru.vs.control.sample.acme_server.domain

import ru.vs.core.acme.model.AcmeAccountId
import ru.vs.core.acme.repository.AcmeAccount
import ru.vs.core.acme.repository.AcmeAccountRepository
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

class AcmeAccountRepositoryImpl : AcmeAccountRepository {
    private val accounts: MutableMap<AcmeAccountId, AcmeAccount> = Collections.synchronizedMap(mutableMapOf())
    private val ids = AtomicInteger()

    override suspend fun add(acmeAccount: AcmeAccount): AcmeAccount {
        assert(acmeAccount.id == null)
        val account = acmeAccount.copy(id = AcmeAccountId(ids.getAndIncrement().toString()))
        accounts[account.id!!] = account
        return account
    }

    override suspend fun find(id: AcmeAccountId): AcmeAccount? {
        return accounts[id]
    }
}