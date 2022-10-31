package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import com.sebastianpakiela.githubexplorer.domain.rule.RxImmediateSchedulerRule
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommitToShareTextUseCaseTest {

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var useCase: CommitToShareTextUseCase

    @Before
    fun setup() {
        useCase = CommitToShareTextUseCase()
    }

    @Test
    fun `Should create message`() {
        val commit = Commit(
            sha = "sha1", date = "01:00:00 01.01.1970", author = "author", message = "message"
        )
        val observer = useCase.execute(commit).test()

        observer.assertValueCount(1)
        val firstValue = observer.values().first()
        assertThat(firstValue, containsString(commit.sha))
        assertThat(firstValue, containsString(commit.date))
        assertThat(firstValue, containsString(commit.author))
        assertThat(firstValue, containsString(commit.message))
    }
}