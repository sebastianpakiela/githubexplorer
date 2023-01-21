package com.sebastianpakiela.githubexplorer.feature.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.CommitToShareTextUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailsViewModel @Inject constructor(
    private val commitToShareTextUseCase: CommitToShareTextUseCase
) : ViewModel() {

    private val _commitListFlow = MutableStateFlow<List<Commit>>(emptyList())
    val commitListFlow: StateFlow<List<Commit>> = _commitListFlow

    private val shareCommitChannel: Channel<String> = Channel(Channel.BUFFERED)
    val shareCommitEventFlow = shareCommitChannel.receiveAsFlow()

    fun initFrom(repoCommitList: RepoCommitList) {
        viewModelScope.launch {
            _commitListFlow.emit(repoCommitList.list)
        }
    }

    fun onShareClick(commit: Commit) {
        viewModelScope.launch {
            val text = commitToShareTextUseCase.execute(commit)
            shareCommitChannel.send(text)
        }
    }
}