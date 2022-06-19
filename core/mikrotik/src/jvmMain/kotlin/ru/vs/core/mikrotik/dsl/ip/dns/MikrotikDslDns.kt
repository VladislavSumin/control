package ru.vs.core.mikrotik.dsl.ip.dns

import ru.vs.core.mikrotik.connection.MikrotikConnectionInternal
import ru.vs.core.mikrotik.connection.execute
import ru.vs.core.mikrotik.dsl.MikrotikDslAbstract
import ru.vs.core.mikrotik.message.ClientMessage

context(MikrotikConnectionInternal)
class MikrotikDslDns internal constructor(parent: MikrotikDslAbstract?) : MikrotikDslAbstract(parent, "dns/") {
    val static = Static()

    inner class Static : MikrotikDslAbstract(this, "static/") {
        suspend fun print(): List<MikrotikDnsRecord> {
            return execute(ClientMessage(path + "print"))
        }
    }
}
