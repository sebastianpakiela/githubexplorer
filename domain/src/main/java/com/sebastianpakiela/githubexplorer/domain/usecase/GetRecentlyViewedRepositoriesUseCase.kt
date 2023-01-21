package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecentlyViewedRepositoriesUseCase @Inject constructor(private val repository: GithubRepository) {

    fun getRecentlyViewedRepositories(): Flow<List<String>> {
        return repository.getRecentlyViewedRepositories()
    }
}