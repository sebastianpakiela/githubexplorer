package com.sebastianpakiela.githubexplorer.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class ViewStateViewModel<S : ViewState, A : Action, E : Effect> : ViewModel(),
    ViewStateOwner<S, A, E> {

    override val viewStateScope: CoroutineScope = viewModelScope

    override val viewStateFlow: MutableStateFlow<S> = MutableStateFlow(getEmptyState())

    protected val _effect: Channel<E> = Channel(Channel.BUFFERED)

    override val effect: Flow<E> = _effect.receiveAsFlow()
}