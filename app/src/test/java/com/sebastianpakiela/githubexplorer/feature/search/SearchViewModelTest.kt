package com.sebastianpakiela.githubexplorer.feature.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRecentlyViewedRepositoriesUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.GetRepositoryDataUseCase
import com.sebastianpakiela.githubexplorer.domain.usecase.UserAndRepoValidationStatus
import com.sebastianpakiela.githubexplorer.domain.usecase.ValidateRepositoryAndUserUseCase
import com.sebastianpakiela.githubexplorer.rule.TestCoroutineRule
import com.sebastianpakiela.githubexplorer.rule.testCollect
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getRecentlyViewedRepositoriesUseCase: GetRecentlyViewedRepositoriesUseCase = mock()
    private val getRepositoryDataUseCase: GetRepositoryDataUseCase = mock()
    private val validateRepositoryAndUserUseCase: ValidateRepositoryAndUserUseCase = mock()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setup() {
        whenever(
            getRecentlyViewedRepositoriesUseCase.getRecentlyViewedRepositories()
        ).thenReturn(flow { emptyList<String>() })

        viewModel = SearchViewModel(
            getRecentlyViewedRepositoriesUseCase,
            getRepositoryDataUseCase,
            validateRepositoryAndUserUseCase
        )
    }

    @Test
    fun `Should query recently viewed list on init`() = runTest {
        val collector: FlowCollector<List<String>> = mock()
        viewModel.recentlyViewedRepositoriesFlow.testCollect(testScheduler, collector)

        verify(collector).emit(emptyList())
        verifyNoMoreInteractions(collector)
    }

    @Test
    fun `Should not query repo on wrong input`() = runTest {
        val input = "input"
        whenever(
            validateRepositoryAndUserUseCase.validateRepositoryAndUserInput(input)
        ).thenReturn(UserAndRepoValidationStatus.NO_SLASH_PRESENT)

        val loadingCollector: FlowCollector<Boolean> = mock()
        val errorCollector: FlowCollector<UserAndRepoValidationStatus> = mock()
        val snackbarCollector: FlowCollector<Unit> = mock()
        val repoCommitListCollector: FlowCollector<RepoCommitList> = mock()

        viewModel.loadingFlow.testCollect(testScheduler, loadingCollector)
        viewModel.errorFlow.testCollect(testScheduler, errorCollector)
        viewModel.goToErrorSnackBarFlow.testCollect(testScheduler, snackbarCollector)
        viewModel.goToDetailsFlow.testCollect(testScheduler, repoCommitListCollector)

        viewModel.queryRepository(input)

        advanceUntilIdle()

        verify(loadingCollector).emit(false)
        verify(errorCollector).emit(UserAndRepoValidationStatus.NO_SLASH_PRESENT)
        verify(snackbarCollector, never()).emit(any())
        verify(repoCommitListCollector, never()).emit(any())
        verify(getRepositoryDataUseCase, never()).getRepository(input)
    }

    @Test
    fun `Should query repo on correct input`() = runTest {
        val input = "input"
        val repoCommitList = RepoCommitList(emptyList())

        whenever(
            validateRepositoryAndUserUseCase.validateRepositoryAndUserInput(input)
        ).thenReturn(UserAndRepoValidationStatus.CORRECT)
        whenever(
            getRepositoryDataUseCase.getRepository(input)
        ).thenReturn(flowOf(repoCommitList))

        val loadingCollector: FlowCollector<Boolean> = mock()
        val errorCollector: FlowCollector<UserAndRepoValidationStatus> = mock()
        val snackbarCollector: FlowCollector<Unit> = mock()
        val repoCommitListCollector: FlowCollector<RepoCommitList> = mock()
        viewModel.loadingFlow.testCollect(testScheduler, loadingCollector)
        viewModel.errorFlow.testCollect(testScheduler, errorCollector)
        viewModel.goToErrorSnackBarFlow.testCollect(testScheduler, snackbarCollector)
        viewModel.goToDetailsFlow.testCollect(testScheduler, repoCommitListCollector)

        viewModel.queryRepository(input)

        advanceUntilIdle()

        verify(loadingCollector).emit(true)
        verify(loadingCollector, times(2)).emit(false)
        verify(errorCollector).emit(UserAndRepoValidationStatus.CORRECT)
        verify(snackbarCollector, never()).emit(any())
        verify(repoCommitListCollector).emit(repoCommitList)
    }
}