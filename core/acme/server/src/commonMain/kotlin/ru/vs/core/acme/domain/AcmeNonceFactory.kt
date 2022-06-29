package ru.vs.core.acme.domain

import ru.vs.core.acme.model.AcmeNonce
import java.nio.ByteBuffer
import java.security.SecureRandom
import java.util.Base64

interface AcmeNonceFactory {
    fun create(): AcmeNonce
}

class AcmeNonceFactoryImpl : AcmeNonceFactory {
    private val random = SecureRandom()

    override fun create(): AcmeNonce {
        //TODO переписать нормально
        val buffer = ByteBuffer.allocate(8 + 24)
        random.nextBytes(buffer.array())
        buffer.putLong(System.currentTimeMillis())
        val raw = Base64.getUrlEncoder().encodeToString(buffer.array())
        return AcmeNonce(raw)
    }
}