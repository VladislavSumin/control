package ru.vs.control.server.feature.dns_sync.domain

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.withContext

interface DnsSyncInteractor {
    suspend fun init()
}

internal class DnsSyncInteractorImpl(
//    private val scope: CoroutineScope
) : DnsSyncInteractor {
    override suspend fun init() = withContext(CoroutineName("DnsSyncInteractor#init")) {

    }
}