package ru.vs.control.server.feature.dns_sync.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

internal interface DnsServersRepository {
    fun observeDnsServers(): Flow<List<DnsServer>>
}

internal class DnsServersRepositoryImpl : DnsServersRepository {
    override fun observeDnsServers(): Flow<List<DnsServer>> {
        return flowOf(listOf(DnsServer("10.255.255.2", 8728, "admin") { "" }))
    }
}
