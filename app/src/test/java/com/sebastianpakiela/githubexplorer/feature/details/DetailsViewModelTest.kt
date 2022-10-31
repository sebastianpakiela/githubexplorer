package com.sebastianpakiela.githubexplorer.feature.details

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import com.sebastianpakiela.githubexplorer.domain.entity.RepoCommitList
import com.sebastianpakiela.githubexplorer.domain.usecase.CommitToShareTextUseCase
import com.sebastianpakiela.githubexplorer.rule.RxImmediateSchedulerRule
import io.reactivex.rxjava3.core.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class DetailsViewModelTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val commitToShareTextUseCase: CommitToShareTextUseCase = mock()

    private lateinit var viewModel: DetailsViewModel

    @Before
    fun setup() {
        viewModel = DetailsViewModel(commitToShareTextUseCase)
    }

    @Test
    fun `Should emit event onShareClick`() {
        val commit = Commit(
            sha = "sha1",
            date = "01:00:00 01.01.1970",
            author = "author",
            message = "message"
        )
        val output = "Output"
        val observer: Observer<String> = mock()
        whenever(commitToShareTextUseCase.execute(commit)).thenReturn(Single.just(output))
        viewModel.shareCommitEvent.observeForever(observer)

        viewModel.onShareClick(commit)

        verify(observer).onChanged(output)
    }

    @Test
    fun `Should init from object`() {
        val observer: Observer<List<Commit>> = mock()
        viewModel.commitList.observeForever(observer)
        val input = emptyList<Commit>()

        viewModel.initFrom(RepoCommitList(input))

        verify(observer).onChanged(input)
    }
}