package com.sebastianpakiela.githubexplorer.domain.usecase

import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import com.sebastianpakiela.githubexplorer.domain.rule.TestCoroutineRule
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class CommitToShareTextUseCaseTest {

    @get:Rule
    val testSchedulerRule = TestCoroutineRule()

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
        val value = useCase.execute(commit)

        assertThat(value, containsString(commit.sha))
        assertThat(value, containsString(commit.date))
        assertThat(value, containsString(commit.author))
        assertThat(value, containsString(commit.message))
    }
}