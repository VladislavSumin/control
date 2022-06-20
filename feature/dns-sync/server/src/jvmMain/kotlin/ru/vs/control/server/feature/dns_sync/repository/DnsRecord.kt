package ru.vs.control.server.feature.dns_sync.repository

internal data class DnsRecord(
    val host: String,
    val ip: String, //todo change type
) {
    companion object
}