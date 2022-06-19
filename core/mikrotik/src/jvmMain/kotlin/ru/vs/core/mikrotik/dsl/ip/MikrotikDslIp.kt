package ru.vs.core.mikrotik.dsl.ip

import ru.vs.core.mikrotik.connection.MikrotikConnectionInternal
import ru.vs.core.mikrotik.dsl.MikrotikDslAbstract
import ru.vs.core.mikrotik.dsl.ip.dns.MikrotikDslDns

context(MikrotikConnectionInternal)
class MikrotikDslIp internal constructor(parent: MikrotikDslAbstract?) : MikrotikDslAbstract(parent, "ip/") {
    val dns = MikrotikDslDns(this)
}