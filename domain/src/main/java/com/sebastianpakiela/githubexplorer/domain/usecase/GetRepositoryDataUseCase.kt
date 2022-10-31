package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import io.reactivex.rxjava3.core.Single
import javax.inject.Inject

class GetRepositoryDataUseCase @Inject constructor(private val repository: GithubRepository) {

    fun getRepository(userAndRepo: String): Single<RepoCommitList> {
        return repository.getRepo(userAndRepo)
    }
}