package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import com.sebastianpakiela.githubexplorer.domain.rule.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetRecentlyViewedRepositoriesUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val repository: GithubRepository = mock()

    private lateinit var useCase: GetRecentlyViewedRepositoriesUseCase

    @Before
    fun setup() {
        useCase = GetRecentlyViewedRepositoriesUseCase(repository)
    }

    @Test
    fun `Should query repository`() {
        whenever(repository.getRecentlyViewedRepositories()).thenReturn(Observable.just(listOf("google/dagger")))

        useCase.getRecentlyViewedRepositories().test()

        verify(repository).getRecentlyViewedRepositories()
    }
}