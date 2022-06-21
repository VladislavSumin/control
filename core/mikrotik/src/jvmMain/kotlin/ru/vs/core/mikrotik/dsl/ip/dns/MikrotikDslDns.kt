package ru.vs.core.mikrotik.dsl.ip.dns

import ru.vs.core.mikrotik.connection.MikrotikConnectionInternal
import ru.vs.core.mikrotik.connection.execute
import ru.vs.core.mikrotik.dsl.MikrotikDslAbstract
import ru.vs.core.mikrotik.dsl.MikrotikId
import ru.vs.core.mikrotik.message.ClientMessage

context(MikrotikConnectionInternal)
class MikrotikDslDns internal constructor(parent: MikrotikDslAbstract?) : MikrotikDslAbstract(parent, "dns/") {
    val static = Static()

    inner class Static internal constructor() : MikrotikDslAbstract(this, "static/") {
        suspend fun print(): List<MikrotikDnsRecord> {
            return execute(ClientMessage(path + "print"))
        }

        suspend fun remove(record: MikrotikDnsRecord) = remove(record.id)

        suspend fun remove(id: MikrotikId) {
            executeUnit(ClientMessage(path + "remove", mapOf("numbers" to id.id)))
        }

        suspend fun add(record: MikrotikDnsRecord) {
            executeUnit(
                ClientMessage(
                    path + "add",
                    mapOf(
                        "name" to record.name,
                        "address" to record.address,
                        "ttl" to record.ttl,
                        "disabled" to record.disabled.toString(),
                        "comment" to record.comment
                    )
                )
            )
        }
    }
}
