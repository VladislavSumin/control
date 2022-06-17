package ru.vs.control.server.feature.dns_sync.domain

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.vs.control.server.feature.dns_sync.repository.DnsServer
import ru.vs.control.server.feature.dns_sync.repository.DnsServersRepository
import ru.vs.core.mikrotik.MikrotikClient
import ru.vs.core.mikrotik.connection.MikrotikConnection

interface DnsSyncInteractor {
    suspend fun init()
}

internal class DnsSyncInteractorImpl(
    private val serversRepository: DnsServersRepository,
    private val mikrotikClient: MikrotikClient,
//    private val scope: CoroutineScope
) : DnsSyncInteractor {
    override suspend fun init() = withContext(CoroutineName("DnsSyncInteractor#init")) {
        logger.i("Init DnsSync feature")

        val servers = serversRepository.observeDnsServers().first()
        servers.forEach { server ->
            mikrotikClient.connect(server) {
                println(execute("/ip/dns/static/print"))
            }
        }
    }

    companion object {
        private val logger = Logger.withTag("DnsSync")
    }
}

private suspend fun MikrotikClient.connect(server: DnsServer, action: suspend MikrotikConnection.() -> Unit) =
    connect(server.host, server.port, server.login, server.password(), action)