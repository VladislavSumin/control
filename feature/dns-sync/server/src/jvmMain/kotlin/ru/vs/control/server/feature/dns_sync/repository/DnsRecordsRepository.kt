package ru.vs.control.server.feature.dns_sync.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import ru.vs.core.coroutines.DispatchersProvider
import java.io.File

internal interface DnsRecordsRepository {
    fun observeDnsRecords(): Flow<List<DnsRecord>>
}

private const val DNS_RECORDS_FILE = "config/dns/records.txt"

private val DNS_REGEX = Regex("^(?<host>.+) (?<ip>.+)\$")

internal class DnsRecordsRepositoryImpl(
    private val dispatchersProvider: DispatchersProvider
) : DnsRecordsRepository {
    override fun observeDnsRecords(): Flow<List<DnsRecord>> {
        return flow { emit(readDnsRecords()) }
    }

    private suspend fun readDnsRecords() = withContext(dispatchersProvider.IO) {
        File(DNS_RECORDS_FILE).readLines()
            .map(DnsRecord.Companion::fromString)
    }
}

private fun DnsRecord.Companion.fromString(data: String): DnsRecord {
    val match = DNS_REGEX.matchEntire(data) ?: throw RuntimeException("wrong dns format")
    val group = match.groups
    return DnsRecord(
        host = group["host"]!!.value,
        ip = group["ip"]!!.value,
    )
}

