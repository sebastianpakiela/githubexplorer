package com.sebastianpakiela.githubexplorer.feature.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.CommitToShareTextUseCase
import com.sebastianpakiela.githubexplorer.rule.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@OptIn(ExperimentalCoroutinesApi::class)
class DetailsViewModelTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val commitToShareTextUseCase: CommitToShareTextUseCase = mock()

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        viewModel = DetailsViewModel(commitToShareTextUseCase)
    }

    @Test
    fun `Should emit event onShareClick`() = runTest {
        val commit = Commit(
            sha = "sha1",
            date = "01:00:00 01.01.1970",
            author = "author",
            message = "message"
        )
        val output = "Output"
        whenever(commitToShareTextUseCase.execute(commit)).thenReturn(output)

        viewModel.onShareClick(commit)
        advanceUntilIdle()

        verify(commitToShareTextUseCase).execute(commit)
        assertThat(viewModel.shareCommitEventFlow.first(), equalTo(output))
    }

    @Test
    fun `Should init from object`() = runTest {
        val input = emptyList<Commit>()

        viewModel.initFrom(RepoCommitList(input))
        advanceUntilIdle()

        assertThat(viewModel.commitListFlow.first(), equalTo(input))
    }
}