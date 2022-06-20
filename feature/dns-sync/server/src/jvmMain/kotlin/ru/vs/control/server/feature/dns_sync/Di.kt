package ru.vs.control.server.feature.dns_sync

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import ru.vs.control.server.feature.dns_sync.domain.DnsSyncInteractor
import ru.vs.control.server.feature.dns_sync.domain.DnsSyncInteractorImpl
import ru.vs.control.server.feature.dns_sync.repository.DnsRecordsRepository
import ru.vs.control.server.feature.dns_sync.repository.DnsRecordsRepositoryImpl
import ru.vs.control.server.feature.dns_sync.repository.DnsServersRepository
import ru.vs.control.server.feature.dns_sync.repository.DnsServersRepositoryImpl
import ru.vs.core.di.Modules
import ru.vs.core.di.i

fun Modules.featureDnsSync() = DI.Module("core-dns-sync") {
    bindSingleton<DnsRecordsRepository> { DnsRecordsRepositoryImpl(i()) }
    bindSingleton<DnsServersRepository> { DnsServersRepositoryImpl(i()) }

    bindSingleton<DnsSyncInteractor> { DnsSyncInteractorImpl(i(), i(), i()) }
}