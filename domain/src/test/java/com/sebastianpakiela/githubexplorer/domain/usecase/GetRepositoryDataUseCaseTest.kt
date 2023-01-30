package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import com.sebastianpakiela.githubexplorer.domain.rule.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class GetRepositoryDataUseCaseTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()
    private val repository: GithubRepository = mock()

    private lateinit var useCase: GetRepositoryDataUseCase

    @Before
    fun setup() {
        useCase = GetRepositoryDataUseCase(repository)
    }

    @Test
    fun `Should query repository`() = runTest {
        val key = "google/dagger"
        whenever(repository.getRepo(key)).thenReturn(flowOf(RepoCommitList(emptyList())))

        useCase.getRepository(key).collect()

        verify(repository).getRepo(key)
    }
}