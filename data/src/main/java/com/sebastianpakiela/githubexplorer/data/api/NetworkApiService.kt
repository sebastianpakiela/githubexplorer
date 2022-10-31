package com.sebastianpakiela.githubexplorer.data.api

import com.sebastianpakiela.githubexplorer.data.entity.network.CommitEntryDto
import com.sebastianpakiela.githubexplorer.data.entity.network.RepositoryDto
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path

interface NetworkApiService {

    @GET("/repos/{userAndRepo}/commits?per_page=100&page=1")
    fun getCommitData(
        @Path(value = "userAndRepo", encoded = true) userAndRepo: String
    ): Single<List<CommitEntryDto>>

    @GET("/repos/{userAndRepo}")
    fun getRepositoryData(
        @Path(value = "userAndRepo", encoded = true) userAndRepo: String
    ): Single<RepositoryDto>
}