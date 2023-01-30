package com.sebastianpakiela.githubexplorer.domain.usecase

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
class GetRecentlyViewedRepositoriesUseCaseTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

    private val repository: GithubRepository = mock()

    private lateinit var useCase: GetRecentlyViewedRepositoriesUseCase

    @Before
    fun setup() {
        useCase = GetRecentlyViewedRepositoriesUseCase(repository)
    }


    @Test
    fun `Should query repository`() = runTest {
        whenever(repository.getRecentlyViewedRepositories()).thenReturn(flowOf((listOf("google/dagger"))))

        useCase.getRecentlyViewedRepositories().collect()

        verify(repository).getRecentlyViewedRepositories()
    }
}