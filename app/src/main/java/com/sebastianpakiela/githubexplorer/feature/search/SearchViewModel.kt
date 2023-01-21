package com.sebastianpakiela.githubexplorer.feature.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRecentlyViewedRepositoriesUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRepositoryDataUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus
import com.sebastianpakiela.githubexplorer.domain.usecase.ValidateRepositoryAndUserUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    getRecentlyViewedRepositoriesUseCase: GetRecentlyViewedRepositoriesUseCase,
    private val getRepositoryDataUseCase: GetRepositoryDataUseCase,
    private val validateRepositoryAndUserUseCase: ValidateRepositoryAndUserUseCase
) : ViewModel() {

    private val _loadingFlow = MutableStateFlow(false)
    val loadingFlow: StateFlow<Boolean> = _loadingFlow

    private val _errorFlow = MutableStateFlow(UserAndRepoValidationStatus.CORRECT)
    val errorFlow: StateFlow<UserAndRepoValidationStatus> = _errorFlow

    val recentlyViewedRepositoriesFlow: StateFlow<List<String>> =
        getRecentlyViewedRepositoriesUseCase.getRecentlyViewedRepositories()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val goToDetailsChannel: Channel<RepoCommitList> = Channel(Channel.BUFFERED)
    val goToDetailsFlow = goToDetailsChannel.receiveAsFlow()

    private val goToErrorSnackBarChannel: Channel<Unit> = Channel(Channel.BUFFERED)
    val goToErrorSnackBarFlow = goToErrorSnackBarChannel.receiveAsFlow()

    fun queryRepository(userAndRepoInput: String) {
        val userAndRepoInput = userAndRepoInput.trim()

        viewModelScope.launch {
            val validationResult =
                validateRepositoryAndUserUseCase.validateRepositoryAndUserInput(userAndRepoInput)

            _errorFlow.emit(validationResult)
            if (validationResult != UserAndRepoValidationStatus.CORRECT) {
                return@launch
            }

            getRepositoryDataUseCase
                .getRepository(userAndRepoInput)
                .catch { exc ->
                    if (exc is HttpException) {
                        goToErrorSnackBarChannel.send(Unit)
                    }
                    Log.e("Error", exc.message, exc)
                }
                .onStart {
                    _loadingFlow.emit(true)
                }
                .onCompletion {
                    _loadingFlow.emit(false)
                }
                .collect { goToDetailsChannel.send(it) }
        }
    }
}