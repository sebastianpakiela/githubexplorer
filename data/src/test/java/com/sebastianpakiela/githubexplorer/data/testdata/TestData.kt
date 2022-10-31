package com.sebastianpakiela.githubexplorer.data.testdata

import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import com.sebastianpakiela.githubexplorer.domain.entity.Commit

object TestData {
    val repoList = listOf("google/dagger", "mockito/mockito-kotlin")

    val repoEntityList = listOf<RecentlyViewedRepositoryEntity>(
        RecentlyViewedRepositoryEntity("google/dagger", 1),
        RecentlyViewedRepositoryEntity("mockito/mockito-kotlin", 1)
    )

    val commitEntity = CommitEntity(
        sha = "sha1",
        date = "01:00:00 01.01.1970",
        author = "author",
        message = "message",
        repoAndUserKey = "repokey"
    )

    val repoEntity = RecentlyViewedRepositoryEntity(
        userAndRepoKey = repoList[0],
        timeStamp = 1
    )

    val commit = Commit(
        sha = "sha1", date = "01:00:00 01.01.1970", author = "author", message = "message"
    )
}