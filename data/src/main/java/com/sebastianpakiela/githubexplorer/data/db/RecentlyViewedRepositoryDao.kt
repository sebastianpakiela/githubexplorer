package com.sebastianpakiela.githubexplorer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable

@Dao
interface RecentlyViewedRepositoryDao {

    @Query("SELECT * FROM rv_repository ORDER BY time_stamp ASC")
    fun getAll(): Observable<List<RecentlyViewedRepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putRepository(recentlyViewedRepositoryEntity: RecentlyViewedRepositoryEntity): Completable
}