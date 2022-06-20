package ru.vs.core.mikrotik.dsl.ip.dns

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MikrotikDnsRecord(
    @SerialName(".id")
    val id: String,
    val name: String,
    val address: String, //InetAddress,
    val ttl: String, //Duration,
    val dynamic: Boolean,
    val disabled: Boolean,
    val comment: String = "",
)