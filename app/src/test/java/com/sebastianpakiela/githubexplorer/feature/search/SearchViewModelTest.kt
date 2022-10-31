package com.sebastianpakiela.githubexplorer.feature.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRecentlyViewedRepositoriesUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRepositoryDataUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus
import com.sebastianpakiela.githubexplorer.domain.usecase.ValidateRepositoryAndUserUseCase
import com.sebastianpakiela.githubexplorer.rule.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

class SearchViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRecentlyViewedRepositoriesUseCase: GetRecentlyViewedRepositoriesUseCase = mock()
    private val getRepositoryDataUseCase: GetRepositoryDataUseCase = mock()
    private val validateRepositoryAndUserUseCase: ValidateRepositoryAndUserUseCase = mock()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        whenever(
            getRecentlyViewedRepositoriesUseCase.getRecentlyViewedRepositories()
        ).thenReturn(Observable.just(emptyList()))

        viewModel = SearchViewModel(
            getRecentlyViewedRepositoriesUseCase,
            getRepositoryDataUseCase,
            validateRepositoryAndUserUseCase
        )
    }

    @Test
    fun `Should query recently viewed list on init`() {
        val observer: Observer<List<String>> = mock()
        viewModel.recentlyViewedRepositories.observeForever(observer)

        verify(observer).onChanged(emptyList())
    }

    @Test
    fun `Should not query repo on wrong input`() {
        val input = "input"
        whenever(validateRepositoryAndUserUseCase.validateRepositoryAndUserInput(input)).thenReturn(
            Single.just(UserAndRepoValidationStatus.NO_SLASH_PRESENT)
        )
        val loadingObserver: Observer<Boolean> = mock()
        val errorObserver: Observer<UserAndRepoValidationStatus> = mock()
        val snackbarObserver: Observer<Unit> = mock()
        val repoCommitList: Observer<RepoCommitList> = mock()
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.errorSnackBarEvent.observeForever(snackbarObserver)
        viewModel.goToDetailsEvent.observeForever(repoCommitList)

        viewModel.queryRepository(input)

        verify(loadingObserver).onChanged(true)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(UserAndRepoValidationStatus.NO_SLASH_PRESENT)
        verify(snackbarObserver, never()).onChanged(any())
        verify(repoCommitList, never()).onChanged(any())
        verify(getRepositoryDataUseCase, never()).getRepository(input)
    }

    @Test
    fun `Should query repo on correct input`() {
        val input = "input"
        val repoCommitList = RepoCommitList(emptyList())

        whenever(validateRepositoryAndUserUseCase.validateRepositoryAndUserInput(input)).thenReturn(
            Single.just(UserAndRepoValidationStatus.CORRECT)
        )
        whenever(getRepositoryDataUseCase.getRepository(input)).thenReturn(
            Single.just(repoCommitList)
        )
        val loadingObserver: Observer<Boolean> = mock()
        val errorObserver: Observer<UserAndRepoValidationStatus> = mock()
        val snackbarObserver: Observer<Unit> = mock()
        val repoCommitListObserver: Observer<RepoCommitList> = mock()
        viewModel.loading.observeForever(loadingObserver)
        viewModel.error.observeForever(errorObserver)
        viewModel.errorSnackBarEvent.observeForever(snackbarObserver)
        viewModel.goToDetailsEvent.observeForever(repoCommitListObserver)

        viewModel.queryRepository(input)

        verify(loadingObserver).onChanged(true)
        verify(loadingObserver).onChanged(false)
        verify(errorObserver).onChanged(UserAndRepoValidationStatus.CORRECT)
        verify(snackbarObserver, never()).onChanged(any())
        verify(repoCommitListObserver).onChanged(repoCommitList)
    }
}