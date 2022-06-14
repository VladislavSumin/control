package ru.vs.core.mikrotik.message

internal data class ServerMessage(val result: Result, val data: Map<String, String>) {

    @Suppress("EnumEntryName")
    enum class Result {
        done,
        trap,
        re,
        fatal,
    }
}