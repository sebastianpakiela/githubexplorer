package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class GetRecentlyViewedRepositoriesUseCase @Inject constructor(private val repository: GithubRepository) {

    fun getRecentlyViewedRepositories(): Observable<List<String>> {
        return repository.getRecentlyViewedRepositories()
    }
}