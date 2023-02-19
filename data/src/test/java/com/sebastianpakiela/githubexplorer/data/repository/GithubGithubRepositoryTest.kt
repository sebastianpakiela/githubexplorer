package com.sebastianpakiela.githubexplorer.data.repository

import com.sebastianpakiela.githubexplorer.data.api.NetworkApiService
import com.sebastianpakiela.githubexplorer.data.db.CommitDao
import com.sebastianpakiela.githubexplorer.data.db.RecentlyViewedRepositoryDao
import com.sebastianpakiela.basetest.rule.TestCoroutineRule
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import javax.inject.Provider

class GithubGithubRepositoryTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

    private val apiService: NetworkApiService = mock()
    private val repositoryDao: RecentlyViewedRepositoryDao = mock()
    private val commitDao: CommitDao = mock()
    private val timestampProvider: Provider<Long> = mock()

    lateinit var repository: GithubRepositoryImpl

    @Before
    fun setup() {
        repository = GithubRepositoryImpl(
            apiService, repositoryDao, commitDao, timestampProvider
        )
    }

    @Test
    fun `Should return recently viewed repositories`() = runTest {
        whenever(repositoryDao.getAll()).thenReturn(flowOf(TestData.repoEntityList))
        val recentRepoCollector: FlowCollector<List<String>> = mock()

        repository.getRecentlyViewedRepositories().collect(recentRepoCollector)

        verify(recentRepoCollector).emit(TestData.repoList)
    }
}


