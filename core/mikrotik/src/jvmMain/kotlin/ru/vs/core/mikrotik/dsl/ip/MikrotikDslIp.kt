package ru.vs.core.mikrotik.dsl.ip

import ru.vs.core.mikrotik.connection.MikrotikConnection
import ru.vs.core.mikrotik.dsl.MikrotikDslAbstract
import ru.vs.core.mikrotik.dsl.ip.dns.MikrotikDslDns

context(MikrotikConnection)
class MikrotikDslIp internal constructor(parent: MikrotikDslAbstract?) : MikrotikDslAbstract(parent, "ip/") {
    val dns = MikrotikDslDns(this)

}