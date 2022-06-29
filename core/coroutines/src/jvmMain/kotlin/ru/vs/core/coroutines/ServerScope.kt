package ru.vs.core.coroutines

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.job
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext

private const val TAG = "ServerScope"

/**
 * This scope automatically closing when server receiving SIGINT
 * @param doOnCancellation - calling after scope is closed
 */
// TODO сейчас doOnCancellation вызовется только при отмене скоупа через shutdown-hook, нужно это поправить
class ServerScope(
    private val doOnCancellation: suspend () -> Unit = {}
) : CoroutineScope {
    override val coroutineContext: CoroutineContext = CoroutineName("server-scope") + Job()

    init {
        setupShutdownHook()
    }

    private fun setupShutdownHook() {
        Runtime.getRuntime().addShutdownHook(
            Thread {
                val logger = Logger.withTag(TAG)
                runBlocking(CoroutineName("shutdown-hook")) {
                    logger.i("Shutdown hook received")

                    // cancel return immediately, don't wait cancellation complete
                    this@ServerScope.cancel("Server closing", ServerClosingException())

                    // because process will be killed immediately after exit outside shutdown huck
                    // we must wait scope shutdown before exit
                    this@ServerScope.coroutineContext.job.join()

                    logger.i("Server scope closed")
                    doOnCancellation()
                    logger.i("Shutdown hook complete")
                }
            }
        )
    }

    /**
     * Block caller thread until cope cancellation
     */
    fun blockingAwait() = runBlocking {
        this@ServerScope.coroutineContext.job.join()
    }
}

class ServerClosingException : RuntimeException()
