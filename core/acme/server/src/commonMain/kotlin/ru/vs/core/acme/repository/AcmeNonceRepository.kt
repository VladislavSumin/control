package ru.vs.core.acme.repository

import ru.vs.core.acme.model.AcmeNonce
import java.time.ZonedDateTime
import java.util.Collections
import java.util.concurrent.atomic.AtomicInteger

interface AcmeNonceRepository {
    suspend fun saveNonce(
        nonce: AcmeNonce,
        expirationDate: ZonedDateTime = ZonedDateTime.now().plusHours(1)
    )

    /**
     * @return true if nonce exists in repository && expirationDate > now(), otherwise false
     */
    suspend fun deleteNonce(nonce: AcmeNonce): Boolean
}

class AcmeNonceRepositoryImpl : AcmeNonceRepository {
    private val nonces: MutableMap<AcmeNonce, ZonedDateTime> = Collections.synchronizedMap(mutableMapOf())
    private val cleanupCounter = AtomicInteger()

    override suspend fun saveNonce(nonce: AcmeNonce, expirationDate: ZonedDateTime) {
        nonces.putIfAbsent(nonce, expirationDate)
        if (cleanupCounter.updateAndGet { (it + 1) % CLEANUP_INTERVAL } == 0) cleanup()
    }

    override suspend fun deleteNonce(nonce: AcmeNonce): Boolean {
        val expDte = nonces.remove(nonce)
        return when {
            expDte == null -> false
            expDte.isBefore(ZonedDateTime.now()) -> true
            else -> false
        }
    }

    private fun cleanup() {
        val now = ZonedDateTime.now()
        synchronized(nonces) {
            val iterator = nonces.iterator()
            for (nonceRecord in iterator) {
                if (nonceRecord.value.isAfter(now)) iterator.remove()
            }
        }
    }

    companion object {
        private const val CLEANUP_INTERVAL = 100
    }
}