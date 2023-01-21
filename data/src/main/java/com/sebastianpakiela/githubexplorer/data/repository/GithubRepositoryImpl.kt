package com.sebastianpakiela.githubexplorer.data.repository

import com.sebastianpakiela.githubexplorer.data.api.NetworkApiService
import com.sebastianpakiela.githubexplorer.data.db.CommitDao
import com.sebastianpakiela.githubexplorer.data.db.RecentlyViewedRepositoryDao
import com.sebastianpakiela.githubexplorer.data.entity.DATE_FORMAT_PATTERN
import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
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

    override fun getRepo(userAndRepo: String): Flow<RepoCommitList> {
        return flow {
            val repoData = apiService.getRepositoryData(userAndRepo)
            val commitData = apiService.getCommitData(userAndRepo)

            val totalData = RepoCommitList(list = commitData.map { it.toDomain(dateFormatter) })

            emit(totalData)

            commitDao.putCommits(totalData.list.map { CommitEntity.fromDomain(it, userAndRepo) })
            repositoryDao.putRepository(
                RecentlyViewedRepositoryEntity(
                    userAndRepo,
                    timestampProvider.get()
                )
            )
        }.catch {
            if (it is SocketTimeoutException || it is UnknownHostException) {
                emitAll(fallbackToDbCacheCommitData(it, userAndRepo))
            } else {
                throw it
            }
        }.flowOn(Dispatchers.IO)
    }

    private fun fallbackToDbCacheCommitData(
        originalError: Throwable,
        userAndRepo: String
    ): Flow<RepoCommitList> {
        return if (originalError is SocketTimeoutException || originalError is UnknownHostException) {
            commitDao.get(userAndRepo).map { it.map { it.toDomain() } }.map { RepoCommitList(it) }
        } else {
            throw originalError
        }
    }

    override fun getRecentlyViewedRepositories(): Flow<List<String>> {
        return repositoryDao.getAll().map { entry -> entry.map { it.userAndRepoKey } }
    }
}