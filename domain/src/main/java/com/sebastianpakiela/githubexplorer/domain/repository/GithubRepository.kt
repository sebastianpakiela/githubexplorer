package com.sebastianpakiela.githubexplorer.domain.repository

import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

interface GithubRepository {

    fun getRepo(userAndRepo: String): Single<RepoCommitList>
    fun getRecentlyViewedRepositories(): Observable<List<String>>
}