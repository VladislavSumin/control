package ru.vs.control.server.feature.dns_sync.domain

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext
import ru.vs.core.mikrotik.MikrotikClient

interface DnsSyncInteractor {
    suspend fun init()
}

internal class DnsSyncInteractorImpl(
    private val mikrotikClient: MikrotikClient,
//    private val scope: CoroutineScope
) : DnsSyncInteractor {
    override suspend fun init() = withContext(CoroutineName("DnsSyncInteractor#init")) {
        logger.i("Init DnsSync feature")

        // Test piece of code
        logger.i("Connecting to 10.255.255.2")
        mikrotikClient.connect("10.255.255.2", username = "admin", password = "") {

        }
    }

    companion object {
        private val logger = Logger.withTag("DnsSync")
    }
}