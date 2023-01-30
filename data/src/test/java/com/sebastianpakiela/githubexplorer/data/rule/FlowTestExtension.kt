package com.sebastianpakiela.githubexplorer.data.rule

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/**
 * Returns a flow that invokes the given [action] **before** each value of the upstream flow is emitted downstream.
 */
@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.testCollect(
    testScheduler: TestCoroutineScheduler,
    collector: FlowCollector<T>
): Job = transform { value ->
    collector.emit(value)
    return@transform emit(value)
}.launchIn(CoroutineScope(UnconfinedTestDispatcher(testScheduler)))