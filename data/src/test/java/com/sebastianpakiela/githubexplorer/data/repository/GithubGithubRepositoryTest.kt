package com.sebastianpakiela.githubexplorer.data.repository

import com.sebastianpakiela.githubexplorer.data.api.NetworkApiService
import com.sebastianpakiela.githubexplorer.data.db.CommitDao
import com.sebastianpakiela.githubexplorer.data.db.RecentlyViewedRepositoryDao
import com.sebastianpakiela.githubexplorer.data.rule.RxImmediateSchedulerRule
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import io.reactivex.rxjava3.core.Observable
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import javax.inject.Provider

class GithubGithubRepositoryTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

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
    fun `Should return recently viewed repositories`() {
        whenever(repositoryDao.getAll()).thenReturn(Observable.just(TestData.repoEntityList))

        val observer = repository.getRecentlyViewedRepositories().test()

        val receivedValues = observer.values()
        assertThat(receivedValues.size, equalTo(1))
        assertThat(receivedValues.first(), equalTo(TestData.repoList))
    }
}


