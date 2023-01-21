package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRepositoryDataUseCase @Inject constructor(private val repository: GithubRepository) {

    fun getRepository(userAndRepo: String): Flow<RepoCommitList> {
        return repository.getRepo(userAndRepo)
    }
}