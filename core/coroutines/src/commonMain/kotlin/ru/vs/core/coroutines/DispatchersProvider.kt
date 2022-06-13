package ru.vs.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainCoroutineDispatcher

@Suppress("PropertyName")
interface DispatchersProvider {
    val Main: MainCoroutineDispatcher
    val Default: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
    val IO: CoroutineDispatcher
}

internal  expect class DispatchersProviderImpl() : DispatchersProvider