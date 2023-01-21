package com.sebastianpakiela.githubexplorer.domain.repository

import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import kotlinx.coroutines.flow.Flow

interface GithubRepository {

    fun getRepo(userAndRepo: String): Flow<RepoCommitList>
    fun getRecentlyViewedRepositories(): Flow<List<String>>
}