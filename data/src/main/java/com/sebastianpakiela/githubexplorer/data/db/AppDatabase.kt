package com.sebastianpakiela.githubexplorer.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sebastianpakiela.githubexplorer.data.entity.db.CommitEntity
import com.sebastianpakiela.githubexplorer.data.entity.db.RecentlyViewedRepositoryEntity

@Database(entities = [RecentlyViewedRepositoryEntity::class, CommitEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun recentlyViewedRepositoriesDao(): RecentlyViewedRepositoryDao

    abstract fun commitDao(): CommitDao
}