package com.sebastianpakiela.githubexplorer.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single

@Dao
interface CommitDao {

    @Query("SELECT * FROM commit_table ORDER BY date ASC")
    fun getAll(): Observable<List<CommitEntity>>

    @Query("SELECT * FROM commit_table WHERE repoAndUserKey = :input ORDER BY date ASC")
    fun get(input: String): Single<List<CommitEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun putCommits(recentlyViewedRepositoryEntity: List<CommitEntity>): Completable
}