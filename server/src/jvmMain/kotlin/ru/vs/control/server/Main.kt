package ru.vs.control.server

import co.touchlab.kermit.Logger
import ru.vs.core.logging.setupDefault
import ru.vs.core.logging.shutdown

fun main() {
    // Setup logger
    Logger.setupDefault()
    Logger.setTag("server")

    Logger.i("Starting server")


    Logger.i("Stopping server")
    Logger.i("Shooting down logger")
    Logger.shutdown()
}