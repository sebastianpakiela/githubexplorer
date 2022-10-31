package com.sebastianpakiela.githubexplorer.data.entity.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rv_repository")
data class RecentlyViewedRepositoryEntity(
    @PrimaryKey val userAndRepoKey: String,
    @ColumnInfo(name = "time_stamp") val timeStamp: Long
)