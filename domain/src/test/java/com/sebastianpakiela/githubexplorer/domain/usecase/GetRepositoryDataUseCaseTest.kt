package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import com.sebastianpakiela.githubexplorer.domain.rule.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class GetRepositoryDataUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private val repository: GithubRepository = mock()

    private lateinit var useCase: GetRepositoryDataUseCase

    @Before
    fun setup() {
        useCase = GetRepositoryDataUseCase(repository)
    }

    @Test
    fun `Should query repository`() {
        val key = "google/dagger"
        whenever(repository.getRepo(key)).thenReturn(Single.just(RepoCommitList(emptyList())))

        useCase.getRepository(key).test()

        verify(repository).getRepo(key)
    }
}