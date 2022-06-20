package ru.vs.core.mikrotik.dsl

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class MikrotikId(val id: String)