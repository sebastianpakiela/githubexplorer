package com.sebastianpakiela.githubexplorer.data.entity.network

import com.sebastianpakiela.githubexplorer.domain.entity.Commit
import java.time.Instant
import java.time.format.DateTimeFormatter

data class CommitEntryDto(
    val sha: String,
    val commit: CommitDto?
) {
    fun toDomain(dateFormatter: DateTimeFormatter): Commit {
        return Commit(
            date = dateFormatter.format(
                commit?.author?.date ?: Instant.now()
            ),
            sha = sha,
            author = commit?.author?.name ?: "Empty",
            message = commit?.message ?: "No message"
        )
    }
}