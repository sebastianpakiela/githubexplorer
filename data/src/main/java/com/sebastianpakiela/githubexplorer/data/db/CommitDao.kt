package com.sebastianpakiela.githubexplorer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommitDao {

    @Query("SELECT * FROM commit_table ORDER BY date ASC")
    fun getAll(): Flow<List<CommitEntity>>

    @Query("SELECT * FROM commit_table WHERE repoAndUserKey = :input ORDER BY date ASC")
    fun get(input: String): Flow<List<CommitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putCommits(recentlyViewedRepositoryEntity: List<CommitEntity>)
}