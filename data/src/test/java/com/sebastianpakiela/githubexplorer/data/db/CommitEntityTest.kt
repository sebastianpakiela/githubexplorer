package com.sebastianpakiela.githubexplorer.data.db

import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import com.sebastianpakiela.githubexplorer.data.testdata.TestData
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class CommitEntityTest {

    @Test
    fun `Should map to domain model`() {
        val commitEntity = TestData.commitEntity

        val result = commitEntity.toDomain()

        assertThat(result.date, equalTo(commitEntity.date))
        assertThat(result.sha, equalTo(commitEntity.sha))
        assertThat(result.message, equalTo(commitEntity.message))
        assertThat(result.author, equalTo(commitEntity.author))
    }

    @Test
    fun `Should map from domain model`() {
        val repoAndUserKey = "repokey"
        val commit = TestData.commit

        val result = CommitEntity.fromDomain(commit, repoAndUserKey)

        assertThat(result.date, equalTo(commit.date))
        assertThat(result.sha, equalTo(commit.sha))
        assertThat(result.message, equalTo(commit.message))
        assertThat(result.author, equalTo(commit.author))
        assertThat(result.repoAndUserKey, equalTo(repoAndUserKey))
    }
}