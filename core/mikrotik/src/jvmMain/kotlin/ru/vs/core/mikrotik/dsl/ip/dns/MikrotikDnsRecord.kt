package ru.vs.core.mikrotik.dsl.ip.dns

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import ru.vs.core.mikrotik.dsl.MikrotikId

@Serializable
data class MikrotikDnsRecord(
    @SerialName(".id")
    val id: MikrotikId = MikrotikId("<new>"),
    val name: String,
    val address: String, //InetAddress,
    val ttl: String = "1d", //Duration,
    val dynamic: Boolean = false,
    val disabled: Boolean = false,
    val comment: String = "",
)