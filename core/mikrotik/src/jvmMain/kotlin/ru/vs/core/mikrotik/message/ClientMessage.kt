package ru.vs.core.mikrotik.message

internal data class ClientMessage(
    val command: String,
    val params: Map<String, String> = emptyMap(),
    val attributes: Map<String, String> = emptyMap(),
    val queries: Map<String, String> = emptyMap(),
) {
    /**
     * To add extensions
     */
    companion object
}