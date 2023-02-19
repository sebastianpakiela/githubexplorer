package com.sebastianpakiela.githubexplorer.feature.search

import com.sebastianpakiela.githubexplorer.base.ViewState
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus

data class SearchViewState(
    val loading: Boolean = false,
    val searchInputValidationStatus: UserAndRepoValidationStatus = UserAndRepoValidationStatus.CORRECT,
    val recentlyViewedRepositories: List<String> = emptyList()
) : ViewState