package ru.vs.core.mikrotik.message

private object MessageParser {
    private val COMMAND_REGEX = Regex("^[/a-z]+")
    private val PARAMETER_REGEX = Regex("(?<key>[a-z]+)=(?<value>[A-z\\d.-]+|\"[A-z\\d .=-]+\")")

    fun parse(input: String): ClientMessage {
        val command = COMMAND_REGEX.find(input)?.value ?: throw IllegalArgumentException("Can`t parse command")
        val arguments = PARAMETER_REGEX.findAll(input)
            .map { it.groups["key"]!!.value to it.groups["value"]!!.value }
            .toMap()

        return ClientMessage(command, arguments)
    }

}

internal fun ClientMessage.Companion.fromString(input: String) = MessageParser.parse(input)