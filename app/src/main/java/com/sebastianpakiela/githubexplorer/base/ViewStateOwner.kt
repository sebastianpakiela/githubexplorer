package com.sebastianpakiela.githubexplorer.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

interface ViewStateOwner<S : ViewState, A : Action, E : Effect> {

    val viewStateScope: CoroutineScope

    fun getEmptyState(): S

    val viewStateFlow: MutableStateFlow<S>

    val effect: Flow<E>

    fun dispatchAction(action: A) {
        viewStateScope.launch {
            val newState = reduceAction(action, viewStateFlow.value)
            viewStateFlow.emit(newState)
        }
    }

    fun dispatchEvent(action: A)

    fun reduceAction(action: A, currentState: S): S
}
