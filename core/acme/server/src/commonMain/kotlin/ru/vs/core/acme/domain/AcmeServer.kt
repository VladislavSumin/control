package ru.vs.core.acme.domain

import ru.vs.core.acme.model.AcmeNonce

interface AcmeServer {
    suspend fun newNonce(): AcmeNonce
    suspend fun newAccount()
}

class AcmeServerImpl(
    private val nonceFactory: AcmeNonceFactory = AcmeNonceFactoryImpl()
) : AcmeServer {
    override suspend fun newNonce(): AcmeNonce {
        return nonceFactory.create()
    }

    override suspend fun newAccount() {

    }
}