package com.sebastianpakiela.githubexplorer.feature.search

import com.sebastianpakiela.githubexplorer.base.Action
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus

sealed class SearchAction : Action

object LoadRecentlyViewedRepositories : SearchAction()

data class RecentlyViewedRepositoriesReceived(
    val recentlyViewedRepositories: List<String>
) : SearchAction()

data class ValidateInput(val input: String) : SearchAction()

data class InputValidateResult(
    val success: Boolean,
    val validationStatus: UserAndRepoValidationStatus,
    val input: String
) : SearchAction()

object ShowSnackbarError : SearchAction()

data class RepositoryDataReceived(val repoCommitList: RepoCommitList) : SearchAction()
