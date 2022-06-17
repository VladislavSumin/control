package ru.vs.control.server.feature.dns_sync.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.vs.core.coroutines.DispatchersProvider
import java.io.File

internal interface DnsServersRepository {
    fun observeDnsServers(): Flow<List<DnsServer>>
}

private const val HOSTS_FILE = "config/dns/hosts.txt"

//admin@10.255.255.2:8728 password
private val HOST_REGEX = Regex("^(?<login>[A-z\\d_-]+)@(?<host>[A-z\\d_\\-.]+):?(?<port>\\d*) (?<password>.*)\$")

internal class DnsServersRepositoryImpl(
    private val dispatchersProvider: DispatchersProvider
) : DnsServersRepository {
    override fun observeDnsServers(): Flow<List<DnsServer>> {
        return flow { emit(readDnsServers()) }
    }

    private suspend fun readDnsServers() = withContext(dispatchersProvider.IO) {
        File(HOSTS_FILE).readLines()
            .map(DnsServer.Companion::fromString)
    }
}

private fun DnsServer.Companion.fromString(data: String): DnsServer {
    val match = HOST_REGEX.matchEntire(data) ?: throw RuntimeException("wrong server format")
    val group = match.groups
    val password = group["password"]!!.value
    return DnsServer(
        host = group["host"]!!.value,
        port = group["port"]?.value?.toInt() ?: 8728,
        login = group["login"]!!.value,
        password = { password },
    )
}

