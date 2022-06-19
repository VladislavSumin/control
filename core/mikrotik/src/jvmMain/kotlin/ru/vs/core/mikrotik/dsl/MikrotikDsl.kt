package ru.vs.core.mikrotik.dsl

import ru.vs.core.mikrotik.connection.MikrotikConnection
import ru.vs.core.mikrotik.dsl.ip.MikrotikDslIp

abstract class MikrotikDslAbstract internal constructor(
    private val parent: MikrotikDslAbstract?,
    private val relativePath: String
) {
    internal val path: String
        get() = (parent?.path ?: "") + relativePath
}

context(MikrotikConnection)
@Suppress("ClassName")
class MikrotikDsl internal constructor() : MikrotikDslAbstract(null, "/") {
    val ip = MikrotikDslIp(this)

}
