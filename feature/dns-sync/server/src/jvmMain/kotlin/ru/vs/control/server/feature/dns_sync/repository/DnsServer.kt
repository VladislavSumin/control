package ru.vs.control.server.feature.dns_sync.repository

internal data class DnsServer(
    val host: String,
    val port: Int = 8728,
    val login: String,
    val password: () -> String
) {
    companion object
}