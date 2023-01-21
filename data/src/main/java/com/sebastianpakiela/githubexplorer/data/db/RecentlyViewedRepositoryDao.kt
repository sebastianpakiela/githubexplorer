package com.sebastianpakiela.githubexplorer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecentlyViewedRepositoryDao {

    @Query("SELECT * FROM rv_repository ORDER BY time_stamp ASC")
    fun getAll(): Flow<List<RecentlyViewedRepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putRepository(recentlyViewedRepositoryEntity: RecentlyViewedRepositoryEntity)
}