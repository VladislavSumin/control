package ru.vs.core.mikrotik.dsl.ip.dns

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vs.core.mikrotik.dsl.MikrotikId

@Serializable
data class MikrotikDnsRecord(
    @SerialName(".id")
    val id: MikrotikId,
    val name: String,
    val address: String, //InetAddress,
    val ttl: String, //Duration,
    val dynamic: Boolean,
    val disabled: Boolean,
    val comment: String = "",
)