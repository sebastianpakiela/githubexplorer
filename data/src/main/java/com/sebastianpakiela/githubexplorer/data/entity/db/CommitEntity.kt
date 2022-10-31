package com.sebastianpakiela.githubexplorer.data.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sebastianpakiela.githubexplorer.domain.entity.Commit

@Entity(tableName = "commit_table")
data class CommitEntity(
    @PrimaryKey val sha: String,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "message") val message: String,
    @ColumnInfo(name = "repoAndUserKey") val repoAndUserKey: String
) {
    fun toDomain(): Commit = Commit(
        date = date,
        sha = sha,
        author = author,
        message = message
    )

    companion object {
        fun fromDomain(
            commit: Commit,
            repoAndUserKey: String
        ) = CommitEntity(
            sha = commit.sha,
            date = commit.date,
            author = commit.author,
            message = commit.message,
            repoAndUserKey = repoAndUserKey
        )
    }
}