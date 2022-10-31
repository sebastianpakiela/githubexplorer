package com.sebastianpakiela.githubexplorer.data.repository

import com.sebastianpakiela.githubexplorer.data.api.NetworkApiService
import com.sebastianpakiela.githubexplorer.data.db.CommitDao
import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import com.sebastianpakiela.githubexplorer.data.db.RecentlyViewedRepositoryDao
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import com.sebastianpakiela.githubexplorer.data.entity.DATE_FORMAT_PATTERN
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider

class GithubRepositoryImpl @Inject constructor(
    private val apiService: NetworkApiService,
    private val repositoryDao: RecentlyViewedRepositoryDao,
    private val commitDao: CommitDao,
    @Named("timestamp") private val timestampProvider: Provider<Long>
) : GithubRepository {

    private val dateFormatter =
        DateTimeFormatter.ofPattern(DATE_FORMAT_PATTERN).withZone(ZoneId.systemDefault())

    override fun getRepo(userAndRepo: String): Single<RepoCommitList> {
        return apiService
            .getRepositoryData(userAndRepo)
            .flatMap { getCommitsData(userAndRepo) }
            .onErrorResumeNext { originalError ->
                fallbackToDbCacheCommitData(originalError, userAndRepo)
            }
    }

    private fun getCommitsData(userAndRepo: String): Single<RepoCommitList> {
        return apiService
            .getCommitData(userAndRepo)
            .map { commitEntryList ->
                RepoCommitList(list = commitEntryList.map { it.toDomain(dateFormatter) })
            }
            .flatMap { result ->
                val entities = result.list.map { commit ->
                    CommitEntity.fromDomain(commit, userAndRepo)
                }

                commitDao.putCommits(entities).toSingleDefault(result)
            }
            .flatMap {
                repositoryDao.putRepository(
                    RecentlyViewedRepositoryEntity(userAndRepo, timestampProvider.get())
                ).toSingleDefault(it)
            }
    }

    private fun fallbackToDbCacheCommitData(
        originalError: Throwable?,
        userAndRepo: String
    ) = if (originalError is SocketTimeoutException || originalError is UnknownHostException) {
        commitDao
            .get(userAndRepo)
            .map { it.map { it.toDomain() } }
            .map { RepoCommitList(it) }
    } else {
        Single.error(originalError)
    }

    override fun getRecentlyViewedRepositories(): Observable<List<String>> {
        return repositoryDao.getAll().map { entry -> entry.map { it.userAndRepoKey } }
    }
}