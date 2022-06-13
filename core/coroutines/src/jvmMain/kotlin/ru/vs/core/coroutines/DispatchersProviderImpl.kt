package ru.vs.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

internal actual class DispatchersProviderImpl : DispatchersProvider {
    override val Main: MainCoroutineDispatcher
        get() = Dispatchers.Main
    override val Default: CoroutineDispatcher
        get() = Dispatchers.Default
    override val Unconfined: CoroutineDispatcher
        get() = Dispatchers.Unconfined
    override val IO: CoroutineDispatcher
        get() = Dispatchers.IO
}