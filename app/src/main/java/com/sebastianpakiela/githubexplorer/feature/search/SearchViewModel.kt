package com.sebastianpakiela.githubexplorer.feature.search

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.sebastianpakiela.githubexplorer.base.ViewStateViewModel
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRecentlyViewedRepositoriesUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRepositoryDataUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus
import com.sebastianpakiela.githubexplorer.domain.usecase.ValidateRepositoryAndUserUseCase
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException
import javax.inject.Inject

class SearchViewModel @Inject constructor(
    private val getRecentlyViewedRepositoriesUseCase: GetRecentlyViewedRepositoriesUseCase,
    private val getRepositoryDataUseCase: GetRepositoryDataUseCase,
    private val validateRepositoryAndUserUseCase: ValidateRepositoryAndUserUseCase
) : ViewStateViewModel<SearchViewState, SearchAction, SearchEffect>() {

//    private val _errorFlow = MutableStateFlow(UserAndRepoValidationStatus.CORRECT)
//    val errorFlow: StateFlow<UserAndRepoValidationStatus> = _errorFlow
//
//    val recentlyViewedRepositoriesFlow: StateFlow<List<String>> =
//        getRecentlyViewedRepositoriesUseCase.getRecentlyViewedRepositories()
//            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
//
//    private val goToDetailsChannel: Channel<RepoCommitList> = Channel(Channel.BUFFERED)
//    val goToDetailsFlow = goToDetailsChannel.receiveAsFlow()
//
//    private val goToErrorSnackBarChannel: Channel<Unit> = Channel(Channel.BUFFERED)
//    val goToErrorSnackBarFlow = goToErrorSnackBarChannel.receiveAsFlow()

    override fun getEmptyState(): SearchViewState {
        return SearchViewState()
    }

    init {
        dispatchAction(LoadRecentlyViewedRepositories)
    }

    override fun reduceAction(
        action: SearchAction,
        currentState: SearchViewState
    ): SearchViewState {
        dispatchEvent(action)

        return when (action) {
            is RecentlyViewedRepositoriesReceived -> {
                currentState.copy(
                    loading = false,
                    recentlyViewedRepositories = action.recentlyViewedRepositories
                )
            }
            is LoadRecentlyViewedRepositories -> {
                currentState.copy(loading = true)
            }
            is RepositoryDataReceived -> {
                currentState.copy(loading = false)
            }
            is InputValidateResult -> currentState.copy(loading = action.success)
            else -> SearchViewState()
        }
    }

    override fun dispatchEvent(action: SearchAction) {
        viewModelScope.launch {
            when (action) {
                is LoadRecentlyViewedRepositories -> queryRecentlyViewedRepositories()
                is ValidateInput -> validateInput(action.input)
                is InputValidateResult -> onInputValidateResult(
                    action.validationStatus,
                    action.input
                )
                is ShowSnackbarError -> _effect.send(ShowSnackbarEffect)

                else -> {}
            }
        }
    }

    private fun validateInput(input: String) {
        viewModelScope.launch {
            val validationResult =
                validateRepositoryAndUserUseCase.validateRepositoryAndUserInput(input)

            val action = InputValidateResult(
                validationResult == UserAndRepoValidationStatus.CORRECT,
                validationResult,
                input
            )
            dispatchAction(action)
        }
    }

    private fun onInputValidateResult(
        validationStatus: UserAndRepoValidationStatus,
        input: String
    ) {
        if (validationStatus != UserAndRepoValidationStatus.CORRECT) {
            return
        }

        viewModelScope.launch {
            getRepositoryDataUseCase.getRepository(input)
                .catch { exc ->
                    if (exc is HttpException) {
                        dispatchAction(ShowSnackbarError)
                        Log.e("Error", exc.message, exc)
                    }
                }
                .collect { dispatchAction(RepositoryDataReceived(it)) }
        }
    }

    private fun queryRecentlyViewedRepositories() {
        viewModelScope.launch {
            getRecentlyViewedRepositoriesUseCase
                .getRecentlyViewedRepositories()
                .collect { dispatchAction(RecentlyViewedRepositoriesReceived(it)) }
        }
    }
}
