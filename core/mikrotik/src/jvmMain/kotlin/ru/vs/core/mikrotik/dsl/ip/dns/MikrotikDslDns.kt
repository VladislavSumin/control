package ru.vs.core.mikrotik.dsl.ip.dns

import ru.vs.core.mikrotik.connection.MikrotikConnection
import ru.vs.core.mikrotik.dsl.MikrotikDslAbstract

context(MikrotikConnection)
class MikrotikDslDns internal constructor(parent: MikrotikDslAbstract?) : MikrotikDslAbstract(parent, "dns/") {
    val static = Static()

    inner class Static : MikrotikDslAbstract(this, "static/") {
        suspend fun print(): List<Map<String, String>> {
            return execute(path + "print")
        }
    }
}