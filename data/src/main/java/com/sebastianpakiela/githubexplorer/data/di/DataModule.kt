package com.sebastianpakiela.githubexplorer.data.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.sebastianpakiela.githubexplorer.data.api.NetworkApiService
import com.sebastianpakiela.githubexplorer.data.db.AppDatabase
import com.sebastianpakiela.githubexplorer.data.db.CommitDao
import com.sebastianpakiela.githubexplorer.data.db.RecentlyViewedRepositoryDao
import com.sebastianpakiela.githubexplorer.data.json.InstantDateDeserializer
import com.sebastianpakiela.githubexplorer.data.repository.GithubRepositoryImpl
import com.sebastianpakiela.githubexplorer.domain.repository.GithubRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Instant
import javax.inject.Named
import javax.inject.Singleton

@Module(includes = [InternalDataModule::class])
abstract class DataModule {

    @Binds
    abstract fun bindRepository(repo: GithubRepositoryImpl): GithubRepository
}

@Module
class InternalDataModule {

    @Provides
    @Singleton
    fun providesRetrofit(
        converterFactory: Converter.Factory,
    ): Retrofit {
        val logging =
            HttpLoggingInterceptor().apply { this.level = HttpLoggingInterceptor.Level.BASIC }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.github.com")
            .addConverterFactory(converterFactory)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().registerTypeAdapter(
            Instant::class.java, InstantDateDeserializer()
        ).create()
    }

    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): NetworkApiService {
        return retrofit.create(NetworkApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoom(context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "appdb").build()
    }

    @Provides
    @Singleton
    fun provideRecentlyViewedDao(db: AppDatabase): RecentlyViewedRepositoryDao {
        return db.recentlyViewedRepositoriesDao()
    }

    @Provides
    @Singleton
    fun provideCommitDao(db: AppDatabase): CommitDao {
        return db.commitDao()
    }

    @Provides
    @Named("timestamp")
    fun provideTimeStamp(): Long = System.currentTimeMillis()
}